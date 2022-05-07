package com.example.article.controller;

import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.service.ArticleServiceImpl;
import com.example.article.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ArticleServiceImpl articleService;
    private final MemberServiceImpl memberService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = "memberId", required = false) Long memberId, Model model){
        List<Article> articles = articleService.findArticles();
        model.addAttribute("articles",articles);

        if(memberId==null) {
            return "home";
        }

        Member member = memberService.findById(memberId);
        model.addAttribute("member",member);
        return "loginedHome";
    }
}
