package com.example.article.api;

import com.example.article.api.error.errorresponse.BasicErrorResponse;
import com.example.article.api.error.errorresponse.MemberErrorResponse;
import com.example.article.api.error.member.MemberException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.example.article.api.error.member.MemberErrorCode.SIZE_NOT_MATCHED;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({MemberException.class})
    public ResponseEntity<ApiResult<MemberErrorResponse>> handleMemberException(MemberException e, HttpServletRequest request){
        log.info("error code: {}, message: {}, url: {}",
                e.getErrorCode(),e.getErrorMessage(),request.getRequestURI());
        MemberErrorResponse response = MemberErrorResponse.builder()
                .memberErrorCode(e.getErrorCode().toString())
                .memberErrorField(ofNullable(e.getErrorField()))
                .errorMessage(e.getErrorMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>(response));
    }

    //@Valid 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<MemberErrorResponse>> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request){

        List<FieldError> findErrors = e.getBindingResult().getFieldErrors();

        List<String> errorFields = new ArrayList<>();

        for (FieldError fieldError : findErrors) {
            errorFields.add(fieldError.getField());
        }

        log.info("error fields: {}, message: {} url: {}",
                e.getBindingResult().getFieldErrors(),e.getMessage(),request.getRequestURI());
        MemberErrorResponse response = MemberErrorResponse.builder()
                .memberErrorCode(SIZE_NOT_MATCHED.toString())
                .memberErrorFields(of((errorFields)))
                .errorMessage(SIZE_NOT_MATCHED.getErrorMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResult<>(response));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResult<BasicErrorResponse>> noHandlerFoundHandle(NoHandlerFoundException e,HttpServletRequest request) {
        log.error("error message: {}, url: {}",e.getMessage(),request.getRequestURI());
        BasicErrorResponse response = BasicErrorResponse.builder()
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>(response));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResult<BasicErrorResponse>> noReadableMessageHandle
            (HttpMessageNotReadableException e,HttpServletRequest request){
        log.error("error message: {}, url:{}",e.getMessage(),request.getRequestURI());
        BasicErrorResponse response = BasicErrorResponse.builder()
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResult<>(response));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<BasicErrorResponse>>  handleServerException(Exception e, HttpServletRequest request){
        log.error("error message: {}, url: {}",e.getMessage(),request.getRequestURI());
        BasicErrorResponse response = BasicErrorResponse.builder()
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResult<>(response));
    }
}
