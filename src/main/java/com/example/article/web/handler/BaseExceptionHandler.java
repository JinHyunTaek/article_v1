package com.example.article.web.handler;

import com.example.article.api.error.BasicException;
import com.example.article.api.error.errorresponse.BasicErrorResponse;
import com.example.article.api.error.errorresponse.SimpleErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@ControllerAdvice("com.example.article.web")
@Slf4j
public class BaseExceptionHandler {

    @ExceptionHandler({BasicException.class})
    public ModelAndView handleMemberException(BasicException e, HttpServletRequest request){
        log.info("error code: {}, message: {}, url: {}",
                e.getErrorCode(),e.getErrorMessage(),request.getRequestURI());
        BasicErrorResponse response = BasicErrorResponse.builder()
                .errorCode(e.getErrorCode().toString())
                .errorFields(List.of(e.getErrorField()))
                .errorMessage(e.getErrorMessage())
                .build();
        if(e.getErrorCode().getStatus() != null){
            return new ModelAndView(
                    "error/"+e.getErrorCode().getStatus(),
                    Map.of(
                            "errorCode",response.getErrorCode(),
                            "message",response.getErrorMessage(),
                            "status",e.getErrorCode().getStatus()
                    ));
        }
        return new ModelAndView(
                "error/4xx",
                Map.of(
                      "errorCode",response.getErrorCode(),
                        "message",response.getErrorMessage()
                ));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView noHandlerFoundHandle(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("error message: {}, url: {}",e.getMessage(),request.getRequestURI());
        SimpleErrorResponse response = SimpleErrorResponse.builder()
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return new ModelAndView(
                "error/404",
                Map.of(
                        "url",response.getUrl(),
                        "message",response.getMessage()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ModelAndView noReadableMessageHandle
            (HttpMessageNotReadableException e,HttpServletRequest request){
        log.error("error message: {}, url:{}",e.getMessage(),request.getRequestURI());
        SimpleErrorResponse response = SimpleErrorResponse.builder()
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return new ModelAndView(
                "error/404",
                Map.of(
                        "url",response.getUrl(),
                        "message",response.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleServerException(
            Exception e, HttpServletRequest request){
        log.error("exception.class, error message: {}, url: {}",e.getMessage(),request.getRequestURI());
        log.info("error",e);
        SimpleErrorResponse response = SimpleErrorResponse.builder()
                .url(request.getRequestURI())
                .message(e.getMessage())
                .build();
        return new ModelAndView(
                "error/500",
                Map.of(
                        "url",response.getUrl(),
                        "message",response.getMessage()
                ));
    }

}
