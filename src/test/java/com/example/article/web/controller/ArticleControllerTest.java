package com.example.article.web.controller;

import com.example.article.repository.article.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ArticleControllerTest {

    @Autowired
     ArticleRepository articleRepository;

//    @Test
//    void showArticlesByCategory() {
//
//        PageRequest pageRequest = PageRequest.of(12, 10, Sort.by(Sort.Direction.DESC, "id"));
//
//        Page<Article> pagedArticles = articleRepository.findEachTop10ByCategory(
//                ArticleCategory.FREE,pageRequest
//        );
//        List<Article> articles = pagedArticles.getContent();
//
//        System.out.println("totalPages: "+pagedArticles.getTotalPages());
//        System.out.println("isFirst: "+pagedArticles.isFirst());
//        System.out.println("hasPrev: "+pagedArticles.hasPrevious());
//        System.out.println("hasNext: "+pagedArticles.hasNext());
//        System.out.println("pageNumber: "+pagedArticles.getPageable().getPageNumber());
//        System.out.println("getPageable: "+pagedArticles.getPageable().toString());
//        System.out.println(":"+pagedArticles.previousPageable().toString());
//    }
}