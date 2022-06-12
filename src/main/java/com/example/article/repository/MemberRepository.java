package com.example.article.repository;

import com.example.article.domain.Member;
import com.example.article.web.projections.NicknameOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByNickname(String nickname);

    Member findByLoginIdAndPassword(String loginId,String password);

    List<NicknameOnly> findProjectionsByNickname(@Param("nickname") String nickname);

    @Query("select m.nickname from Member m where m.nickname in :nicknames")
    List<String> findNicknameList(@Param("nicknames")Collection<String> nicknames);
}
