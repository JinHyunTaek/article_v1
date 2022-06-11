package com.example.article.jparepository;

import com.example.article.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository {

    void save(Member member);

    Member findById(Long id);

    Optional<Member> findByLoginId(String loginId);

    List<Member> findAll();

    Optional<Member> findByNickname(String nickname);

}
