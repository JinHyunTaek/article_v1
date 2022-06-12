package com.example.article.web.controller;

import com.example.article.api.error.BasicErrorCode;
import com.example.article.api.error.BasicException;
import com.example.article.domain.Article;
import com.example.article.domain.ArticleCategory;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import com.example.article.repository.ArticleRepository;
import com.example.article.repository.MemberRepository;
import com.example.article.service.ArticleService;
import com.example.article.service.MemberService;
import com.example.article.service.ReplyService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.article.api.error.BasicErrorCode.NO_MEMBER_CONFIGURED;
import static java.util.stream.Collectors.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String home(@SessionAttribute(name = "memberId", required = false) Long memberId,
                       @PageableDefault(sort = "id",direction = Sort.Direction.DESC, size = 10) Pageable pageable,
                       Model model) {

        Page<Article> pagedArticles = articleRepository.findAll(pageable);
        List<Article> articles = pagedArticles.getContent();

        model.addAttribute("articleCategories",ArticleCategory.values());
        model.addAttribute("articles", articles);

        if (memberId == null) {
            return "home";
        }

        System.out.println("memberId="+memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED));

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
                    map(reply -> new ReplyDto(reply)).collect(toList());
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
