package com.example.article.service;

import com.example.article.domain.Article;
import com.example.article.repository.ArticleRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepositoryImpl articleRepository;

    @Transactional
    @Override
    public void save(Article article) {
        articleRepository.save(article);
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public List<Article> findArticlesByPageDesc() {
        return articleRepository.findAllByPageDesc();
    }

    @Transactional
    @Override
    public void updateArticle(Long articleId, String title, String body){
        Article article = articleRepository.findById(articleId);
        article.update(title,body);
    }

    @Transactional
    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteArticle(id);
    }

    @Transactional
    @Override
    public void addHitCount(Long articleId) {
        Article article = articleRepository.findById(articleId);
        article.addHitCount();
    }

    @Override
    public List<Article> findArticlesByMemberIdDesc(Long memberId) {
        return articleRepository.findAllByMemberIdDesc(memberId);
    }


}
