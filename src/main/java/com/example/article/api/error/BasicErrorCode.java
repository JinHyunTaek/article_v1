package com.example.article.api.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum BasicErrorCode {

    NO_ARTICLE_CONFIGURED("no_article","게시물 식별 불가"),
    NO_MEMBER_CONFIGURED("no_member","회원 식별 불가"),
    NO_REPLY_CONFIGURED("no_reply","댓글 식별 불가"),
    ID_OR_PASSWORD_NOT_MATCHED("idOrPassword","아이디 혹은 비밀번호가 일치하지 않습니다."),

    DUPLICATED_MEMBER_NICKNAME("dup_nickname","중복된 회원 닉네임"),
    DUPLICATED_MEMBER_LOGIN_ID("dup_loginId","중복된 회원 아이디"),
    DUPLICATED_MEMBER_PASSWORD("dup_password","중복된 비밀번호"),

    SIZE_NOT_MATCHED("size_not_matched","공백이거나 사이즈 오류"),

    UNAUTHORIZED("access is denied to invalid credentials","자격 증명 안 됨",401),
    FORBIDDEN("you don't have permission to access","접근 권한이 없습니다",403),

    INTERNAL_SERVER_ERROR("server_error","서버 장애 발생",500);

    private final String errorField;
    private final String errorMessage;
    private Integer status;
}
