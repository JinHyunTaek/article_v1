package com.example.article.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        if(request.getQueryString()!=null) {
            requestURI = request.getRequestURI() + "?" + request.getQueryString();
        }

        String uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID,uuid);

        HttpSession session = request.getSession(false);
        if(session==null||session.getAttribute("memberId")==null){
            log.info("미인증 사용자 요청, url: {}",requestURI);
            response.sendRedirect("/member/login?redirectURL=" +requestURI);
            return false;
        }

        return true;
    }
}
