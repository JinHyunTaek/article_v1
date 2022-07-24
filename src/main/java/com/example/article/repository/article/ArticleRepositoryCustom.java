package com.example.article.repository;

import com.example.article.condition.article.ArticleBasicCondition;
import com.example.article.condition.article.ArticleSearchCondition;
import com.example.article.domain.Article;
import com.example.article.web.dto.SimpleArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    Page<SimpleArticleDto> searchArticle(ArticleSearchCondition condition, Pageable pageable);
    Page<SimpleArticleDto> findArticleByBasicCondition(ArticleBasicCondition condition, Pageable pageable);
    Page<Article> findArticleForHome(Pageable pageable);
}
