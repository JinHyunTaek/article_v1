package com.example.article.crud;

import com.example.article.api.error.BasicException;
import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.constant.Address;
import com.example.article.repository.MemberRepository;
import com.example.article.repository.article.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.example.article.api.error.BasicErrorCode.ARTICLE_NOT_FOUND;
import static com.example.article.domain.constant.ArticleCategory.FREE;
import static com.example.article.domain.constant.MemberLevel.NEW;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class ArticleBasicCRUD {

    private final String TEST_TITLE="test title1";

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void initArticle(){
        //given
        Address address = Address.builder()
                .city("test city123")
                .street("test street123")
                .zipcode("test zipcode123")
                .build();

        Member member = Member.builder()
                .nickname("test nickname1")
                .memberLevel(NEW)
                .loginId("id-123")
                .password("pw-123")
                .address(address)
                .build();
        memberRepository.save(member);

        Article article = Article.builder()
                .title(TEST_TITLE)
                .body("test body1")
                .member(member)
                .articleCategory(FREE)
                .hit(0)
                .build();
        articleRepository.save(article);
    }

    @Transactional(readOnly = true)
    @Test
    @DisplayName("전체 게시물 조회")
    void read(){
        //when
        List<Article> findArticles = articleRepository.findAll();
        //then
        assertThat(findArticles.get(0).getTitle()).isEqualTo(TEST_TITLE);
        assertThat(findArticles.get(0).getBody()).isEqualTo("test body1");
    }

    @Test
    @DisplayName("게시물 수정(dirty checking)")
    void update(){
        //when
        articleRepository.findByTitle(TEST_TITLE)
                .ifPresentOrElse(
                        article -> article.update("updated title1","updated body1"),
                        () -> {
                            throw new BasicException(ARTICLE_NOT_FOUND);
                        }
                );

        em.flush();
        em.clear();

        //then
        Article updateArticle = articleRepository.findByTitle("updated title1")
                .orElseThrow(() -> new BasicException(ARTICLE_NOT_FOUND));
        assertThat(updateArticle.getBody()).isEqualTo("updated body1");
        assertThat(updateArticle.getArticleCategory()).isEqualTo(FREE);
    }

    @Test
    @DisplayName("게시물 삭제")
    void delete(){
        //when
        articleRepository.deleteAll();
        System.out.println("===");
        em.flush();
        em.clear();
        System.out.println("===");
        //then
        Optional<Article> findArticle = articleRepository.findByTitle(TEST_TITLE);
        assertThat(findArticle).isEmpty();
    }
}
