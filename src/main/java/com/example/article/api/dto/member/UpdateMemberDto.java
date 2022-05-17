package com.example.article.api.dto.member;

import com.example.article.domain.Member;
import com.example.article.domain.MemberLevel;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateMemberDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMemberRequest{

        @NotNull
        private Long id;

        @NotBlank
        @Size(min = 2, max = 10)
        private String nickname;

        @NotBlank
        @Size(min = 2, max = 10)
        private String loginId;

        @NotBlank
        @Size(min = 2, max = 12)
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateMemberResponse{

        private String nickname;
        private MemberLevel memberLevel;
        private String loginId;

        public static UpdateMemberResponse fromEntity(Member member){
            return UpdateMemberResponse.builder()
                    .nickname(member.getNickname())
                    .memberLevel(member.getMemberLevel())
                    .loginId(member.getLoginId())
                    .build();
        }
    }
}
