package com.example.article.jparepository;

import com.example.article.domain.Article;

import java.util.List;

public interface ArticleJpaRepository {

    void save(Article article);

    Article findById(Long id);

    List<Article> findAllByPageDesc();

    void deleteArticle(Long id);

    List<Article> findAllByMemberIdDesc(Long memberId);

}
