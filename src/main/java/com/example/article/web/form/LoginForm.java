package com.example.article.web.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
public class LoginForm {

    @NotEmpty(message = "아이디를 입력해주세요.")
    @Size(min = 3, max = 10)
    private String loginId;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 3, max = 10)
    private String password;

}
