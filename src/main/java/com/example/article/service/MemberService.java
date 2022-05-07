package com.example.article.service;

import com.example.article.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    void saveMember(Member member);

    Member login(String loginId,String password);

    Member findById(Long memberId);
}
