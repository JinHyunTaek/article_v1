package com.example.article.api.error;

import lombok.Getter;

@Getter
public class BasicException extends RuntimeException{

    private BasicErrorCode errorCode;
    private String errorMessage;
    private String errorField;

    public BasicException(Throwable cause, BasicErrorCode errorCode, String errorMessage, String errorField) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorField = errorField;
    }

    public BasicException(BasicErrorCode errorCode, Throwable cause){
        super(cause);
        this.errorCode = errorCode;
        this.errorField = errorCode.getErrorField();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public BasicException(BasicErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorField = errorCode.getErrorField();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public BasicException(BasicErrorCode errorCode, String errorField){
        this.errorCode = errorCode;
        this.errorField = errorField;
        this.errorMessage = errorCode.getErrorMessage();
    }

}
