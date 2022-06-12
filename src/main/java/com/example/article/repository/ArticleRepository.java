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

    Page<Article> findAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    Page<Article> findAllByArticleCategory(ArticleCategory articleCategory, Pageable pageable);

    List<Article> findByMemberIdOrderByIdDesc(@Param("memberId") Long memberId);

    void deleteById(Long articleId);
}
