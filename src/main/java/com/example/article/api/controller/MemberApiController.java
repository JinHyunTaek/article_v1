package com.example.article.api.controller;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.member.CreateMemberDto.CreateMemberRequest;
import com.example.article.api.dto.member.CreateMemberDto.CreateMemberResponse;
import com.example.article.api.dto.member.GetMemberDto;
import com.example.article.api.dto.member.LoginMemberDto.LoginMemberRequest;
import com.example.article.api.dto.member.LoginMemberDto.LoginMemberResponse;
import com.example.article.api.dto.member.UpdateMemberDto.UpdateMemberRequest;
import com.example.article.api.dto.member.UpdateMemberDto.UpdateMemberResponse;
import com.example.article.api.error.member.MemberErrorCode;
import com.example.article.api.error.member.MemberException;
import com.example.article.domain.Member;
import com.example.article.domain.MemberLevel;
import com.example.article.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping("/api/member")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberServiceImpl memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginMemberResponse> login(
            @RequestBody @Valid LoginMemberRequest request, HttpServletRequest servletRequest
    ){
        Member loginMember = memberService.login(request.getLoginId(), request.getPassword());

        if(loginMember==null){
            throw new MemberException(MemberErrorCode.ID_OR_PASSWORD_NOT_MATCHED,"id or password");
        }

        HttpSession session = servletRequest.getSession();
        session.setAttribute("loginId",loginMember.getId());

        LoginMemberResponse response = LoginMemberResponse.toDto(loginMember);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutV3(HttpServletRequest request){
        HttpSession session = request.getSession(false); //가져오되, 생성은 x
        if(session!=null){ //세션 있으면
            session.invalidate();
            return new ResponseEntity<>("logout success",HttpStatus.OK);
        }
        return new ResponseEntity<>("no session",HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/members")
    public List<GetMemberDto> getMembers(){
        List<Member> members = memberService.findAll();
        List<GetMemberDto> memberDtoList =
                members.stream().map(member -> GetMemberDto.toDtoWithAr(member)).collect(Collectors.toList());
        return memberDtoList;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResult<CreateMemberResponse>> create(
            @RequestBody @Valid CreateMemberRequest request
    ){
        Member member = Member.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .memberLevel(MemberLevel.NEW)
                .build();

        if(memberService.findByNickname(member.getNickname())!=null){
            throw new MemberException(MemberErrorCode.DUPLICATED_MEMBER_NICKNAME,"nickname");
        }

        memberService.saveMember(member);
        ApiResult<CreateMemberResponse> response = CreateMemberResponse.toDto(member);

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @GetMapping("/detail/{memberId}")
    public ResponseEntity<GetMemberDto> getDetail(@PathVariable Long memberId){
        Member member = memberService.findById(memberId);
        GetMemberDto memberDto = GetMemberDto.toDtoWithArAndRep(member);

        return new ResponseEntity<>(memberDto,HttpStatus.OK);
    }

    @PostMapping("/update/{memberId}")
    public ResponseEntity<UpdateMemberResponse> update
            (@PathVariable Long memberId,
             @Valid @RequestBody UpdateMemberRequest request
            ){
        Member member = memberService.findById(memberId);
        memberService.updateMember(memberId, request.getLoginId(), request.getPassword(), request.getNickname());
        UpdateMemberResponse response = UpdateMemberResponse.toDto(member);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
