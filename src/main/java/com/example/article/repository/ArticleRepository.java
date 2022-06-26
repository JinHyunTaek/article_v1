package com.example.article.repository;

import com.example.article.domain.Article;
import com.example.article.domain.ArticleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {

    @Query(value = "select a from Article a left join a.member m where a.member.id=:memberId",
            countQuery = "select count(a) from Article a")
    Page<Article> findAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    Page<Article> findAllByArticleCategory(ArticleCategory articleCategory, Pageable pageable);

    List<Article> findByMemberIdOrderByIdDesc(@Param("memberId") Long memberId);

    void deleteById(Long articleId);

    @Override
    @EntityGraph(attributePaths = {"member"})
    Page<Article> findAll(Pageable pageable);
}
