package com.example.article.jparepository;

import com.example.article.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleJpaRepositoryImpl implements ArticleJpaRepository {

    private final EntityManager em;

    public void save(Article article) {
        em.persist(article);
    }

    public Article findById(Long id) {
        return em.find(Article.class,id);
    }

    public List<Article> findAllByPageDesc() {
        return em.createQuery("select a from Article a " +
                        "join fetch a.member order by a.id desc ",Article.class)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();
    }

    @Override
    public void deleteArticle(Long id) {
        System.out.println("==");
        em.createQuery("delete from Reply r where r.article.id = :id")
                .setParameter("id",id)
                .executeUpdate();
        em.createQuery("delete from Article a where a.id =:id")
                .setParameter("id",id)
                .executeUpdate();
        System.out.println("==");
    }

    @Override
    public List<Article> findAllByMemberIdDesc(Long memberId) {
        return em.createQuery("select a from Article a " +
                        "join fetch a.member m " +
                        "where m.id=:memberId order by a.id desc ")
                .setParameter("memberId",memberId)
                .getResultList();
    }

}
