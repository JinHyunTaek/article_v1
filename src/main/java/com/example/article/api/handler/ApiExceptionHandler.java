package com.example.article.api.handler;

import com.example.article.api.ApiResult;
import com.example.article.api.error.BasicException;
import com.example.article.api.error.errorresponse.BasicErrorResponse;
import com.example.article.api.error.errorresponse.SimpleErrorResponse;
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

import static com.example.article.api.error.BasicErrorCode.SIZE_NOT_MATCHED;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler({BasicException.class})
    public ResponseEntity<ApiResult<BasicErrorResponse>> handleMemberException(BasicException e, HttpServletRequest request){
        log.info("error code: {}, message: {}, url: {}",
                e.getErrorCode(),e.getErrorMessage(),request.getRequestURI());
        BasicErrorResponse response = BasicErrorResponse.builder()
                .errorCode(e.getErrorCode().toString())
                .errorFields(List.of(e.getErrorField()))
                .errorMessage(e.getErrorMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResult<>(response));
    }

    //@Valid 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<BasicErrorResponse>> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request){

        List<FieldError> findErrors = e.getBindingResult().getFieldErrors();

        List<String> errorFields = new ArrayList<>();

        for (FieldError fieldError : findErrors) {
            errorFields.add(fieldError.getField());
        }

        log.info("error fields: {}, message: {} url: {}",
                e.getBindingResult().getFieldErrors(),e.getMessage(),request.getRequestURI());
        BasicErrorResponse response = BasicErrorResponse.builder()
                .errorCode(SIZE_NOT_MATCHED.toString())
                .errorFields((errorFields))
                .errorMessage(SIZE_NOT_MATCHED.getErrorMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResult<>(response));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResult<SimpleErrorResponse>> noHandlerFoundHandle(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("error message: {}, url: {}",e.getMessage(),request.getRequestURI());
        SimpleErrorResponse response = SimpleErrorResponse.builder()
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResult<>(response));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResult<SimpleErrorResponse>> noReadableMessageHandle
            (HttpMessageNotReadableException e,HttpServletRequest request){
        log.info("error message: {}, url:{}",e.getMessage(),request.getRequestURI(),e);
        SimpleErrorResponse response = SimpleErrorResponse.builder()
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResult<>(response));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<SimpleErrorResponse>>  handleServerException(
            Exception e, HttpServletRequest request){
        log.error("exception.class, error message: {}, url: {}",e.getMessage(),request.getRequestURI(),e);
        SimpleErrorResponse errorResponse = SimpleErrorResponse.builder()
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResult<>(errorResponse));
    }
}
