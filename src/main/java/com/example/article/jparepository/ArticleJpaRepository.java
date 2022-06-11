package com.example.article.repository;

import com.example.article.domain.Article;

import java.util.List;

public interface ArticleRepository {

    void save(Article article);

    Article findById(Long id);

    List<Article> findAllByPageDesc();

    void deleteArticle(Long id);

    List<Article> findAllByMemberIdDesc(Long memberId);

}
