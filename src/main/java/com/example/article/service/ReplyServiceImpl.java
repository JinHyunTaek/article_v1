package com.example.article.service;

import com.example.article.domain.Article;
import com.example.article.domain.Reply;
import com.example.article.repository.ReplyRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepositoryImpl replyRepository;

    @Transactional
    @Override
    public void save(Reply reply) {
        replyRepository.save(reply);
    }

    @Override
    public Reply findById(Long id) {
        return replyRepository.findById(id);
    }

    @Override
    public List<Reply> findReplies() {
        return replyRepository.findAll();
    }

    @Transactional
    @Override
    public void updateReply(Long replyId, String body) {

    }

    @Transactional
    @Override
    public void deleteReply(Long id) {

    }

    @Override
    public List<Reply> findByArticleId(Long articleId) {
        return replyRepository.findByArticleId(articleId);
    }

    @Override
    public List<Reply> findRepliesOfArticles(List<Article> articles) {
        return replyRepository.findRepliesOfArticles(articles);
    }

    @Override
    public List<Reply> findRepliesByMemberIdDesc(Long memberId) {
        return replyRepository.findAllByMemberIdDesc(memberId);
    }
}
