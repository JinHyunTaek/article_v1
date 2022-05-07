package com.example.article.repository;

import com.example.article.domain.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepository{

    private final EntityManager em;

    @Override
    public void save(Reply reply) {
        em.persist(reply);
    }

    @Override
    public Reply findById(Long id) {
        return em.find(Reply.class,id);
    }

    @Override
    public List<Reply> findAll() {
        return em.createQuery("select r from Reply r",Reply.class)
                .getResultList();
    }

    @Override
    public List<Reply> findByArticleId(Long articleId) {
        return em.createQuery("select r from Reply r where r.article.id=:articleId",Reply.class)
                .setParameter("articleId",articleId)
                .getResultList();
    }

    @Override
    public void deleteReply(Long id) {
        em.createQuery("delete from Reply r where r.id =:id")
                .setParameter("id",id)
                .executeUpdate();
    }
}
