package com.example.article;

import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.dto.ArticleDto;
import com.example.article.dto.CreateMemberDto;
import com.example.article.service.ArticleServiceImpl;
import com.example.article.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final MemberServiceImpl memberService;
    private final ArticleServiceImpl articleService;

    @PostConstruct
    public void init(){
        CreateMemberDto memberDto = new CreateMemberDto("test", "test!", "테스터");
        Member member = memberDto.toEntity();
        memberService.saveMember(member);

        ArticleDto articleDto = new ArticleDto("title1", member, "hello");
        Article article = articleDto.toEntity();
        articleService.save(article);
        System.out.println("article = " + article);
    }
}
