package com.example.article.api.dto.member;

import com.example.article.api.ApiResult;
import com.example.article.domain.Member;
import com.example.article.domain.constant.MemberLevel;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginMemberDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginMemberRequest{

        @NotEmpty
        @NotNull
        @Size(min = 2,max = 10)
        private String loginId;

        @NotEmpty
        @Size(min = 2,max = 10)
        private String password;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LoginMemberResponse{
        private String nickname;
        private MemberLevel memberLevel;

        public static ApiResult<LoginMemberResponse> toDto(Member member){
            return new ApiResult<>(LoginMemberResponse.builder()
                    .nickname(member.getNickname())
                    .memberLevel(member.getMemberLevel())
                    .build());
        }
    }
}
