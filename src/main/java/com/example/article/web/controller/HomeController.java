package com.example.article.web.controller;

import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import com.example.article.service.ArticleServiceImpl;
import com.example.article.service.MemberServiceImpl;
import com.example.article.service.ReplyServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ArticleServiceImpl articleService;
    private final MemberServiceImpl memberService;
    private final ReplyServiceImpl replyService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = "memberId", required = false) Long memberId,
                       Model model) {

        List<Article> articles = articleService.findArticlesByPageDesc();

        model.addAttribute("articles", articles);

        if (memberId == null) {
            return "home";
        }

        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);

        return "loginHome";
    }

    @Data
    static class ArticleDto{
        private Long id;
        private String title;
        private String body;
        private LocalDateTime createDateTime;
        private LocalDateTime modifiedDateTime;
        private Integer likeNumber;
        private Integer hit;
        private Member member;
        private List<ReplyDto> replies;

        public ArticleDto(Article article) {
            id = article.getId();
            title = article.getTitle();
            body = article.getBody();
            createDateTime = article.getCreateDateTime();
            modifiedDateTime = article.getModifiedDateTime();
            likeNumber = article.getLikeNumber();
            hit = article.getHit();
            member = article.getMember();
            replies = article.getReplies().stream().
                    map(reply -> new ReplyDto(reply)).collect(Collectors.toList());
        }
    }

    @Data
    static class ReplyDto{
        private Long id;
        private Article article;
        private Member member;
        private Integer likeNumber;
        private String body;

        public ReplyDto(Reply reply) {
            id = reply.getId();
            article = reply.getArticle();
            member = reply.getMember();
            likeNumber = reply.getLikeNumber();
            body = reply.getBody();
        }
    }
}
