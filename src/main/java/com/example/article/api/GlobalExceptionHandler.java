package com.example.article.api;

import com.example.article.api.error.errorresponse.BasicErrorResponse;
import com.example.article.api.error.errorresponse.MemberErrorResponse;
import com.example.article.api.error.member.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.example.article.api.error.member.MemberErrorCode.INTERNAL_SERVER_ERROR;
import static com.example.article.api.error.member.MemberErrorCode.SIZE_NOT_MATCHED;
import static java.util.Optional.*;
import static java.util.Optional.ofNullable;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //
    @ExceptionHandler({MemberException.class})
    public ResponseEntity<MemberErrorResponse> handleMemberException(MemberException e, HttpServletRequest request){
        log.info("error code: {}, message: {}, url: {}",
                e.getErrorCode(),e.getErrorMessage(),request.getRequestURI());
        MemberErrorResponse response = MemberErrorResponse.builder()
                .memberErrorCode(e.getErrorCode().toString())
                .memberErrorField(ofNullable(e.getErrorField()))
                .errorMessage(e.getErrorMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    //@Valid 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MemberErrorResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request){

        List<FieldError> findErrors = e.getBindingResult().getFieldErrors();

        List<String> errorFields = new ArrayList<>();

        for (FieldError fieldError : findErrors) {
            errorFields.add(fieldError.getField());
        }

        log.info("error field: {}, message: {} url: {}",
                e,e.getMessage(),request.getRequestURI());
        MemberErrorResponse response = MemberErrorResponse.builder()
                .memberErrorCode(SIZE_NOT_MATCHED.toString())
                .memberErrorFields(of((errorFields)))
                .errorMessage(SIZE_NOT_MATCHED.getErrorMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BasicErrorResponse> noHandlerFoundHandle(NoHandlerFoundException e,HttpServletRequest request) {
        log.error("error message: {}, url: {}",e.getMessage(),request.getRequestURI());
        BasicErrorResponse response = BasicErrorResponse.builder()
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MemberErrorResponse> handleServerException(Exception e, HttpServletRequest request){
        log.error("error message: {}, url: {}",e.getMessage(),request.getRequestURI());
        MemberErrorResponse response = MemberErrorResponse.builder()
                .errorMessage(INTERNAL_SERVER_ERROR.getErrorMessage() + " [부가 오류 메세지:"+e.getMessage()+"]")
                .build();
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
