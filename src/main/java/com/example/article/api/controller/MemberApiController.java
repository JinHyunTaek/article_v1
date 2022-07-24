package com.example.article.api.controller;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.member.CreateMemberDto.CreateMemberRequest;
import com.example.article.api.dto.member.CreateMemberDto.CreateMemberResponse;
import com.example.article.api.dto.member.GetMemberDto;
import com.example.article.api.dto.member.LoginMemberDto.LoginMemberRequest;
import com.example.article.api.dto.member.LoginMemberDto.LoginMemberResponse;
import com.example.article.api.dto.member.UpdateMemberDto.UpdateMemberRequest;
import com.example.article.api.dto.member.UpdateMemberDto.UpdateMemberResponse;
import com.example.article.api.service.MemberApiService;
import com.example.article.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequestMapping("/api/member")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberApiService memberApiService;
    private final MemberRepository memberRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResult<LoginMemberResponse>> login(
            @RequestBody @Valid LoginMemberRequest request, HttpServletRequest servletRequest
    ){
        ApiResult<LoginMemberResponse> loginResponse = memberApiService.login(request, servletRequest);

        return ResponseEntity
                .ok()
                .body(loginResponse);
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
    public Page<ApiResult<GetMemberDto>> getMembers(
            @PageableDefault(sort = "id")Pageable pageable
            ){
        Page<ApiResult<GetMemberDto>> memberDtoList = memberApiService.findMembers(pageable);

        return memberDtoList;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResult<CreateMemberResponse>> create(
            @RequestBody @Valid CreateMemberRequest request
    ){
        ApiResult<CreateMemberResponse> response = memberApiService.create(request);

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @GetMapping("/detail/{memberId}")
    public ResponseEntity<ApiResult<GetMemberDto>> getDetail(@PathVariable Long memberId){
        ApiResult<GetMemberDto> memberDto = memberApiService.detail(memberId);

        return ResponseEntity.ok().body(memberDto);
    }

    @PostMapping("/update/{memberId}")
    public ResponseEntity<ApiResult<UpdateMemberResponse>> update
            (@PathVariable Long memberId,
             @Valid @RequestBody UpdateMemberRequest request
            ){
        ApiResult<UpdateMemberResponse> updateResponse = memberApiService.update(memberId, request);
        return new ResponseEntity<>(updateResponse,HttpStatus.OK);
    }

}
