package com.example.article.repository;

import com.example.article.domain.Article;

import java.util.List;

public interface ArticleRepository {

    void save(Article article);

    Article findById(Long id);

    List<Article> findAll();

    void deleteArticle(Long id);

}
