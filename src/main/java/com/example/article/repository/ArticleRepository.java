package com.example.article.repository;

import com.example.article.domain.Article;
import com.example.article.domain.ArticleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    Page<Article> findAll(Pageable pageable);

    Page<Article> findAllByArticleCategory(ArticleCategory articleCategory, Pageable pageable);

    List<Article> findByMemberIdOrderByIdDesc(@Param("memberId") Long memberId);

    @Transactional
    @Modifying(clearAutomatically = true) //변경하는 것을 인지
    @Query("update Article a set a.title = :title, a.body = :body where a.id = :articleId")
    void updateArticle(@Param("articleId") Long articleId, @Param("title") String title, @Param("body") String body);
}
