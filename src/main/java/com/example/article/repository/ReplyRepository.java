package com.example.article.repository;

import com.example.article.domain.Reply;
import com.example.article.web.projections.NicknameOnly;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Query("select count(r) from Reply r where r.article.id=:articleId")
    Long getReplyCount(@Param("articleId") Long articleId);

    List<Reply> findAllByMemberId(Long memberId, Sort sort);

    @Query("select r from Reply r where r.article.id=:articleId")
    List<Reply> findByArticleId(@Param("articleId") Long articleId);

}
