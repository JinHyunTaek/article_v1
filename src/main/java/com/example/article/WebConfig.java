package com.example.article;

import com.example.article.api.service.FileApiService;
import com.example.article.web.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final FileApiService fileApiService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/css/**","/*.ico","/error",
                        "/member/signUp","/member/login","/api/**","/member/logout",
                        "/member/checkLoginId",
                        "/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs",
                        "/webjars/**");
    }

}
