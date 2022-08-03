package com.example.article.repository;

import com.example.article.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import javax.persistence.OrderBy;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Query("select count(r) from Reply r where r.article.id=:articleId")
    Long getReplyCount(@Param("articleId") Long articleId);

//    @Query("select count(r) from Reply r where r.member.id=:memberId")
    List<Reply> findAllByMemberId(Long memberId);

    Page<Reply> findByMemberId(Long memberId, Pageable pageable);

    @Query("select r from Reply r where r.article.id=:articleId")
    List<Reply> findByArticleId(@Param("articleId") Long articleId);

    @Query("select r from Reply r join fetch r.member m where " +
            "r.article.id = :articleId and r.parent is null ")
    List<Reply> findWithMemberByArticleId(@Param("articleId") Long articleId);

    @Query("select r from Reply r join fetch r.member m where r.parent.id=:parentId")
    List<Reply> findByParentId(@Param("parentId") Long parentId);
}
