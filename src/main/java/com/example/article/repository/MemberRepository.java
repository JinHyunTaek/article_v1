package com.example.article.repository;

import com.example.article.domain.Member;
import com.example.article.web.projections.IdAndNickname;
import com.example.article.web.projections.NicknameOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {


//    Page<Member> findAll(Pageable pageable);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByLoginIdAndPassword(String loginId,String password);

    Optional<Member> findByLoginId(String loginId);

    List<NicknameOnly> findProjectionsByNickname(@Param("nickname") String nickname);

    Optional<IdAndNickname> findIdNicknameById(@Param("id") Long memberId);

}
