package com.example.article.repository;

import com.example.article.domain.Member;
import com.example.article.web.projections.IdAndNickname;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
//@Rollback(value = false)
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void projectionTest(){
        Member member = Member.builder()
                .nickname("hello")
                .build();
        em.persist(member);

        em.flush();
        em.clear();

        IdAndNickname result = memberRepository.findIdNicknameById(1L).get();
        System.out.println("member:"+result.getId());
        System.out.println("member:"+result.getNickname());
    }

}