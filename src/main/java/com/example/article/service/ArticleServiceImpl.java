package com.example.article.service;

import com.example.article.domain.Article;
import com.example.article.jparepository.ArticleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleJpaRepository articleJpaRepository;

    @Transactional
    @Override
    public void save(Article article) {
        articleJpaRepository.save(article);
    }

    @Override
    public Article findById(Long id) {
        return articleJpaRepository.findById(id);
    }

    @Override
    public List<Article> findArticlesByPageDesc() {
        return articleJpaRepository.findAllByPageDesc();
    }

    @Transactional
    @Override
    public void updateArticle(Long articleId, String title, String body){
        Article article = articleJpaRepository.findById(articleId);
        article.update(title,body);
    }

    @Transactional
    @Override
    public void deleteArticle(Long id) {
        articleJpaRepository.deleteArticle(id);
    }

    @Transactional
    @Override
    public void addHitCount(Long articleId) {
        Article article = articleJpaRepository.findById(articleId);
        article.addHitCount();
    }

    @Override
    public List<Article> findArticlesByMemberIdDesc(Long memberId) {
        return articleJpaRepository.findAllByMemberIdDesc(memberId);
    }


}
