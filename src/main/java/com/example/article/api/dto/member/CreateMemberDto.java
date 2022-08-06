package com.example.article.api.dto.member;

import com.example.article.api.ApiResult;
import com.example.article.domain.Member;
import com.example.article.domain.constant.MemberLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class CreateMemberDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateMemberRequest{

        @NotEmpty
        @NotNull
        @Size(min = 2,max = 10)
        private String loginId;

        @NotEmpty
        @Size(min = 2,max = 12)
        private String password;

        @NotEmpty
        @Size(min = 2,max = 10)
        private String nickname;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateMemberResponse{
        private String nickname;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime joinedAt;

        private MemberLevel memberLevel;

       public static ApiResult<CreateMemberResponse> toDto(Member member){
           return new ApiResult<>(CreateMemberResponse.builder()
                   .nickname(member.getNickname())
                   .joinedAt(member.getCreatedDate())
                   .memberLevel(member.getMemberLevel())
                   .build());
       }
    }
}
