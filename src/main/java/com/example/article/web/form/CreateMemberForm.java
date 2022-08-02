package com.example.article.web.form;

import com.example.article.domain.Member;
import com.example.article.domain.nonentity.MemberLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@Getter @Setter
@AllArgsConstructor
public class CreateMemberForm {

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
                .memberLevel(MemberLevel.NEW)
                .articles(new ArrayList<>())
                .build();
    }

    public Member toEntityForTester(){
        return Member.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .memberLevel(MemberLevel.ADMIN)
                .articles(new ArrayList<>())
                .build();
    }
}
