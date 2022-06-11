package com.example.article.repository;

import com.example.article.domain.Article;
import com.example.article.domain.Reply;

import java.util.List;

public interface ReplyRepository {

    void save(Reply reply);

    Reply findById(Long id);

    List<Reply> findAll();

    List<Reply> findByArticleId(Long articleId);

    void deleteReply(Long id);

    List<Reply> findRepliesOfArticles(List<Article> articles);

    List<Reply> findAllByMemberIdDesc(Long memberId);
}
