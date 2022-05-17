package com.example.article.service;

import com.example.article.domain.Member;

import java.util.List;

public interface MemberService {

    void saveMember(Member member);

    Member login(String loginId,String password);

    Member findById(Long memberId);

    Member findByNickname(String nickname);

    List<Member> findAll();

    void updateMember(Long id, String loginId, String password, String nickname);
}
