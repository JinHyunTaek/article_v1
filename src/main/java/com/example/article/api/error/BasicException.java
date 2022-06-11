package com.example.article.api.error.member;

import com.example.article.api.error.member.MemberErrorCode;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Getter
public class MemberException extends RuntimeException{

    private MemberErrorCode errorCode;
    private String errorMessage;
    private String errorField;

    public MemberException(MemberErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorField = errorCode.getErrorField();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public MemberException(MemberErrorCode errorCode,String errorField){
        this.errorCode = errorCode;
        this.errorField = errorField;
        this.errorMessage = errorCode.getErrorMessage();
    }

}
