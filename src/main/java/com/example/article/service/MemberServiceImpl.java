package com.example.article.service;

import com.example.article.domain.Member;
import com.example.article.jparepository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{

    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    @Override
    public void saveMember(Member member) {
        memberJpaRepository.save(member);
    }

    @Override
    public Member login(String loginId,String password) {
        return memberJpaRepository.findByLoginId(loginId)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    public Member findById(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }

    @Override
    public Member findByNickname(String nickname) {
        return memberJpaRepository.findByNickname(nickname).orElse(null);
    }

    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAll();
    }

    @Transactional
    @Override
    public void updateMember(Long id, String loginId, String password, String nickname) {
        Member member = memberJpaRepository.findById(id);
        member.update(loginId, password, nickname);
    }
}
