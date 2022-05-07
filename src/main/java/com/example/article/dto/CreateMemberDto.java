package com.example.article.dto;

import com.example.article.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class CreateMemberDto {

    @NotEmpty
    @Size(min = 3, max = 10)
    private String loginId;

    @NotEmpty
    @Size(min=3,max=10)
    private String password;

    @NotEmpty
    private String nickname;

    public Member toEntity(){
        return Member.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .joinedAt(LocalDateTime.now())
                .build();
    }
}
