package com.example.article.web.controller;

import com.example.article.api.error.BasicException;
import com.example.article.domain.Article;
import com.example.article.domain.ArticleCategory;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import com.example.article.repository.article.ArticleRepository;
import com.example.article.repository.MemberRepository;
import com.example.article.condition.article.ArticleSearchCondition.ArticleSearchConditionValue;
import com.example.article.web.service.HomeWebService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.article.api.error.BasicErrorCode.NO_MEMBER_CONFIGURED;
import static java.util.stream.Collectors.toList;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    private final HomeWebService homeService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = "memberId", required = false) Long memberId,
//                       @PageableDefault(sort = "id",direction = Sort.Direction.DESC, size = 10) Pageable pageable,
                       Model model) {
        homeService.setHomeCondition(model);

        if (memberId == null) {
            return "home";
        }

        model.addAttribute("member", homeService.findById(memberId));
        return "loginHome";
    }


}
