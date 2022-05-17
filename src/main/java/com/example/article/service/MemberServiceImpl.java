package com.example.article.service;

import com.example.article.domain.Member;
import com.example.article.repository.MemberRepositoryImpl;
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

    private final MemberRepositoryImpl memberRepository;

    @Transactional
    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member login(String loginId,String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(member -> member.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname).orElse(null);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Transactional
    @Override
    public void updateMember(Long id, String loginId, String password, String nickname) {
        Member member = memberRepository.findById(id);
        member.update(loginId, password, nickname);
    }
}
