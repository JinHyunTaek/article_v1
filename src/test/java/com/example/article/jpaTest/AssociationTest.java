package com.example.article.jpaTest;

import com.example.article.domain.Article;
import com.example.article.domain.ArticleCategory;
import com.example.article.domain.Member;
import com.example.article.domain.MemberLevel;
import com.example.article.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class AssociationTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void article_association(){
        Member member = new Member("new-test","new-test","new-test");
        memberRepository.save(member);

        Article article = new Article("new-test","new-test",0);
        article.setMember(member);
        em.persist(article);


//        Member findMember = em.createQuery("select m from Member m where " +
//                        "m.nickname=:nickname", Member.class)
//                .setParameter("nickname", "new-test")
//                .getSingleResult();
//        System.out.println("article.member="+article.getMember());
        System.out.println("member.getArticles="+member.getArticles());
    }
}
