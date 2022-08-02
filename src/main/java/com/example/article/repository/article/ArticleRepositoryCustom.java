package com.example.article.repository.article;

import com.example.article.condition.article.ArticleBasicCondition;
import com.example.article.condition.article.ArticleSearchCondition;
import com.example.article.domain.nonentity.ArticleCategory;
import com.example.article.domain.Reply;
import com.example.article.web.dto.SimpleArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleRepositoryCustom {
    Page<SimpleArticleDto> search(ArticleSearchCondition condition, Pageable pageable);
    Page<SimpleArticleDto> findByBasicCondition(ArticleBasicCondition condition, Pageable pageable);
    Page<SimpleArticleDto> findByReplies(List<Reply> replies, Pageable pageable);
    List<SimpleArticleDto> findTop10ByCategory(ArticleCategory articleCategory);
}
