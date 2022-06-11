package com.example.article.jparepository;

import com.example.article.domain.Article;
import com.example.article.domain.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReplyJpaRepositoryImpl implements ReplyJpaRepository {

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

    @Override
    public List<Reply> findRepliesOfArticles(List<Article> articles) {
        return em.createQuery("select r from Reply r " +
                "join r.article a",Reply.class)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public List<Reply> findAllByMemberIdDesc(Long memberId) {
        return em.createQuery("select r from Reply r " +
                        "join fetch r.member m " +
                        "where m.id=:memberId")
                .setParameter("memberId",memberId)
                .getResultList();
    }

}
