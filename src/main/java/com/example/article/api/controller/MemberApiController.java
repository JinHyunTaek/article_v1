package com.example.article.api.controller;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.member.CreateMemberDto.CreateMemberRequest;
import com.example.article.api.dto.member.CreateMemberDto.CreateMemberResponse;
import com.example.article.api.dto.member.GetMemberDto;
import com.example.article.api.dto.member.LoginMemberDto.LoginMemberRequest;
import com.example.article.api.dto.member.LoginMemberDto.LoginMemberResponse;
import com.example.article.api.dto.member.UpdateMemberDto.UpdateMemberRequest;
import com.example.article.api.dto.member.UpdateMemberDto.UpdateMemberResponse;
import com.example.article.api.error.BasicErrorCode;
import com.example.article.api.error.BasicException;
import com.example.article.domain.Member;
import com.example.article.domain.MemberLevel;
import com.example.article.service.MemberService;
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
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequestMapping("/api/member")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<ApiResult<LoginMemberResponse>> login(
            @RequestBody @Valid LoginMemberRequest request, HttpServletRequest servletRequest
    ){
        Member loginMember = memberService.login(request.getLoginId(), request.getPassword());

        if(loginMember==null){
            throw new BasicException(BasicErrorCode.ID_OR_PASSWORD_NOT_MATCHED,"id or password");
        }

        HttpSession session = servletRequest.getSession();
        session.setAttribute("loginId",loginMember.getId());

        ApiResult<LoginMemberResponse> loginResponse = LoginMemberResponse.toDto(loginMember);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResult<String>> logoutV3(HttpServletRequest request){
        HttpSession session = request.getSession(false); //가져오되, 생성은 x
        if(session!=null){ //세션 있으면
            session.invalidate();
            return ResponseEntity.ok().body(new ApiResult<>("logout success"));
        }
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(new ApiResult<>("logout failed (세션 정보: " + session.getAttributeNames()+")"));
    }

    @GetMapping("/members")
    public List<ApiResult<GetMemberDto>> getMembers(){
        List<Member> members = memberService.findAll();

        List<ApiResult<GetMemberDto>> memberDtoList = members.stream().map(GetMemberDto::toDtoWithAr)
                .collect(Collectors.toList());
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
            throw new BasicException(BasicErrorCode.DUPLICATED_MEMBER_NICKNAME,"nickname");
        }

        memberService.saveMember(member);
        ApiResult<CreateMemberResponse> response = CreateMemberResponse.toDto(member);

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @GetMapping("/detail/{memberId}")
    public ResponseEntity<ApiResult<GetMemberDto>> getDetail(@PathVariable Long memberId){
        Member member = memberService.findById(memberId);
        ApiResult<GetMemberDto> memberDto = GetMemberDto.toDtoWithArAndRep(member);

        return ResponseEntity.ok().body(memberDto);
    }

    @PostMapping("/update/{memberId}")
    public ResponseEntity<ApiResult<UpdateMemberResponse>> update
            (@PathVariable Long memberId,
             @Valid @RequestBody UpdateMemberRequest request
            ){
        Member member = memberService.findById(memberId);
        memberService.updateMember(memberId, request.getLoginId(), request.getPassword(), request.getNickname());
        ApiResult<UpdateMemberResponse> updateResponse = UpdateMemberResponse.toDto(member);
        return new ResponseEntity<>(updateResponse,HttpStatus.OK);
    }
}
