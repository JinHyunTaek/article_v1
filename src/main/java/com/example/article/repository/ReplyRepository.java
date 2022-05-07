package com.example.article.repository;

import com.example.article.domain.Reply;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository {

    void save(Reply reply);

    Reply findById(Long id);

    List<Reply> findAll();

    List<Reply> findByArticleId(Long articleId);

    void deleteReply(Long id);

}
