package com.example.article.web.controller;

import com.example.article.domain.Article;
import com.example.article.domain.ArticleCategory;
import com.example.article.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@SpringBootTest
@Transactional
class ArticleControllerTest {

    @Autowired
     ArticleRepository articleRepository;

    @Test
    void showArticlesByCategory() {

        PageRequest pageRequest = PageRequest.of(12, 10, Sort.by(Sort.Direction.DESC, "id"));

        Page<Article> pagedArticles = articleRepository.findAllByArticleCategory(
                ArticleCategory.FREE,pageRequest
        );
        List<Article> articles = pagedArticles.getContent();

        System.out.println("totalPages: "+pagedArticles.getTotalPages());
        System.out.println("isFirst: "+pagedArticles.isFirst());
        System.out.println("hasPrev: "+pagedArticles.hasPrevious());
        System.out.println("hasNext: "+pagedArticles.hasNext());
        System.out.println("pageNumber: "+pagedArticles.getPageable().getPageNumber());
        System.out.println("getPageable: "+pagedArticles.getPageable().toString());
        System.out.println(":"+pagedArticles.previousPageable().toString());
    }
}