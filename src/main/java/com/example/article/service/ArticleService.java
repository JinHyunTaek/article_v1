package com.example.article.service;

import com.example.article.domain.Article;

import java.util.List;

public interface ArticleService {

    void save(Article article);

    Article findById(Long id);

    List<Article> findArticles();

    void updateArticle(Long articleId, String title, String body);

    void deleteArticle(Long id);
}
