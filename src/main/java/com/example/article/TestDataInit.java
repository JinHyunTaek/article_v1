package com.example.article;

import com.example.article.domain.Article;
import com.example.article.domain.constant.ArticleCategory;
import com.example.article.domain.Member;
import com.example.article.repository.MemberRepository;
import com.example.article.repository.article.ArticleRepository;
import com.example.article.web.form.article.CreateForm;
import com.example.article.web.form.CreateMemberForm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile("local")
@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @PostConstruct
    public void previousInit(){
        CreateMemberForm memberDto = new CreateMemberForm("test", "test!", "테스터");
        Member member = memberDto.toEntityForTester();

        CreateMemberForm memberDto2 = new CreateMemberForm("123", "123", "테스터2");
        Member member2 = memberDto2.toEntityForTester();

        memberRepository.save(member);
        memberRepository.save(member2);

        CreateForm articleDto = new CreateForm("title1", "hello", ArticleCategory.FREE,member,null,null);

        List<Member> members = new ArrayList<>();
        members.add(member);
        members.add(member2);

        testPageable(members);

        Article article = articleDto.toEntity();
        articleRepository.save(article);
    }

    private void testPageable(List<Member> members){
        List<ArticleCategory> articleCategories = Arrays.stream(ArticleCategory.values()).toList();
        for (Member member : members) {
            for (ArticleCategory articleCategory : articleCategories) {
                for (int i = 0; i < 60; i++) {
                    CreateForm articleForm = new CreateForm(
                            "test-title" + i+" "+articleCategory, "test-body", articleCategory,member,null,null
                    );
                    Article article = articleForm.toEntity();
                    articleRepository.save(article);
                }
            }
        }
    }

}
