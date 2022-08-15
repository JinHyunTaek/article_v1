package com.example.article.api.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum BasicErrorCode {

    ARTICLE_NOT_FOUND("article not found exception","게시물 식별 불가"),
    FILE_NOT_FOUND("file not found exception","파일 식별 불가"),
    MEMBER_NOT_FOUND("member not found exception","회원 식별 불가"),
    REPLY_NOT_FOUND("reply not found exception","댓글 식별 불가"),
    ID_OR_PASSWORD_NOT_MATCHED("idOrPassword","아이디 혹은 비밀번호가 일치하지 않습니다."),

    DUPLICATED_MEMBER_NICKNAME("dup_nickname","중복된 회원 닉네임"),
    DUPLICATED_MEMBER_LOGIN_ID("dup_loginId","중복된 회원 아이디"),
    DUPLICATED_MEMBER_PASSWORD("dup_password","중복된 비밀번호"),

    SIZE_NOT_MATCHED("size_not_matched","공백이거나 사이즈 오류"),

    UNAUTHORIZED("access is denied to invalid credentials","자격 증명 안 됨",401),
    FORBIDDEN("you don't have permission to access","접근 권한이 없습니다",403),

    INTERNAL_SERVER_ERROR("server_error","서버 장애 발생",500),
    DATA_ACCESS_ERROR("data access error","데이터 접근 장애 발생",500);

    private final String errorField;
    private final String errorMessage;
    private Integer status;
}
