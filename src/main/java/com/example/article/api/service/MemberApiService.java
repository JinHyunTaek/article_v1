package com.example.article.api.service;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.member.CreateMemberDto;
import com.example.article.api.dto.member.GetMemberDto;
import com.example.article.api.dto.member.LoginMemberDto;
import com.example.article.api.dto.member.LoginMemberDto.LoginMemberRequest;
import com.example.article.api.dto.member.LoginMemberDto.LoginMemberResponse;
import com.example.article.api.dto.member.UpdateMemberDto;
import com.example.article.api.dto.member.UpdateMemberDto.UpdateMemberResponse;
import com.example.article.api.error.BasicErrorCode;
import com.example.article.api.error.BasicException;
import com.example.article.domain.Member;
import com.example.article.domain.MemberLevel;
import com.example.article.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.example.article.api.dto.member.CreateMemberDto.*;
import static com.example.article.api.error.BasicErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberApiService {

    private final MemberRepository memberRepository;

    public ApiResult<LoginMemberResponse> login
            (LoginMemberRequest request, HttpServletRequest servletRequest) {

        Member loginMember = memberRepository.findByLoginIdAndPassword(request.getLoginId(), request.getPassword())
                .orElseThrow(() -> new BasicException(BasicErrorCode.ID_OR_PASSWORD_NOT_MATCHED));

        HttpSession session = servletRequest.getSession();
        session.setAttribute("loginId",loginMember.getId());

        ApiResult<LoginMemberResponse> loginResponse = LoginMemberResponse.toDto(loginMember);
        return loginResponse;
    }

    public Page<ApiResult<GetMemberDto>> findMembers(Pageable pageable) {
        Page<Member> pagedMembers = memberRepository.findAll(pageable);

        Page<ApiResult<GetMemberDto>> memberDtoList = pagedMembers.map(member -> GetMemberDto.toDtoWithAr(member));
        return memberDtoList;
    }

    public ApiResult<GetMemberDto> detail(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED));
        ApiResult<GetMemberDto> memberDto = GetMemberDto.toDtoWithArAndRep(member);
        return memberDto;
    }

    @Transactional
    public ApiResult<CreateMemberResponse> create(CreateMemberRequest request) {

        memberRepository.findByNickname(request.getNickname())
                .ifPresent(nickname -> { throw new BasicException(DUPLICATED_MEMBER_NICKNAME); });

        memberRepository.findByLoginId(request.getLoginId())
                .ifPresent(idPw -> {throw new BasicException(DUPLICATED_MEMBER_LOGIN_ID);});

        Member member = Member.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .memberLevel(MemberLevel.NEW)
                .build();

        memberRepository.save(member);
        ApiResult<CreateMemberResponse> response = CreateMemberResponse.toDto(member);
        return response;
    }

    @Transactional
    public ApiResult<UpdateMemberResponse> update(Long memberId, UpdateMemberDto.UpdateMemberRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED));
        member.update(request.getLoginId(), request.getPassword(), request.getNickname());
        ApiResult<UpdateMemberResponse> response = UpdateMemberResponse.toDto(member);
        return response;
    }

}
