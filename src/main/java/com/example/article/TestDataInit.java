package com.example.article;

import com.example.article.domain.Article;
import com.example.article.domain.ArticleCategory;
import com.example.article.domain.Member;
import com.example.article.web.form.CreateArticleForm;
import com.example.article.web.form.CreateMemberForm;
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
        CreateMemberForm memberDto = new CreateMemberForm("test", "test!", "테스터");
        Member member = memberDto.toEntityForTester();

        CreateMemberForm memberDto2 = new CreateMemberForm("123", "123", "테스터2");
        Member member2 = memberDto2.toEntityForTester();

        memberService.saveMember(member);
        memberService.saveMember(member2);

        CreateArticleForm articleDto = new CreateArticleForm("title1", member, "hello", ArticleCategory.FREE);

        testPageableFree(member);
        testPageableGreet(member2);

        Article article = articleDto.toEntity();
        articleService.save(article);
    }

    private void testPageableFree(Member member) {
        for(int i=0;i<120;i++){
            CreateArticleForm articleForm = new CreateArticleForm(
                    "test-title", member, "test-body", ArticleCategory.FREE
            );
            Article article = articleForm.toEntity();
            articleService.save(article);
        }
    }

    private void testPageableGreet(Member member) {
        for(int i=0;i<120;i++){
            CreateArticleForm articleForm = new CreateArticleForm(
                    "test-title2", member, "test-body2", ArticleCategory.GREET
            );
            Article article = articleForm.toEntity();
            articleService.save(article);
        }
    }
}
