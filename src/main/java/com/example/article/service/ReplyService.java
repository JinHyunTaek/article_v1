package com.example.article.service;

import com.example.article.domain.Article;
import com.example.article.domain.Reply;

import java.util.List;

public interface ReplyService {

    void save(Reply reply);

    Reply findById(Long id);

    List<Reply> findReplies();

    void updateReply(Long replyId, String body);

    void deleteReply(Long id);

    List<Reply> findByArticleId(Long articleId);
}
