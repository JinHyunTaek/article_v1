package com.example.article.api.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BasicErrorCode {

    NO_MEMBER_CONFIGURED("no_member","접근 권한 없음"),
    ID_OR_PASSWORD_NOT_MATCHED("idOrPassword","아이디 혹은 비밀번호가 일치하지 않습니다."),
    DUPLICATED_MEMBER_NICKNAME("dup_nickname","중복된 회원 닉네임"),
    DUPLICATED_MEMBER_LOGIN_ID("dup_loginId","중복된 회원 아이디"),
    SIZE_NOT_MATCHED("size_not_matched","공백이거나 사이즈 오류"),
    INTERNAL_SERVER_ERROR("server_error","서버 장애 발생");

    private final String errorField;
    private final String errorMessage;
}
