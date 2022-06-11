package com.example.article.repository;

import com.example.article.domain.Article;
import com.example.article.domain.ArticleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    Page<Article> findAll(Pageable pageable);
    Page<Article> findAllByArticleCategory(ArticleCategory articleCategory, Pageable pageable);
}
