package com.example.article.repository;

import com.example.article.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository{

    private final EntityManager em;

    public void save(Article article) {
        em.persist(article);
    }

    public Article findById(Long id) {
        return em.find(Article.class,id);
    }

    public List<Article> findAll() {
        return em.createQuery("select a from Article a join fetch a.member")
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

}
