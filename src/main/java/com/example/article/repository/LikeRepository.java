package com.example.article.repository;

import com.example.article.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface LikeRepository extends JpaRepository<Likes,Long> {

    Optional<Likes> findByMemberIdAndArticleId(Long memberId, Long articleId);

    Optional<List<Likes>> findByArticleId(Long articleId);

    void deleteByArticleIdAndMemberId(Long articleId,Long memberId);
}
