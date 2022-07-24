package com.example.article.web.service;

import com.example.article.domain.Member;
import com.example.article.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

@RequiredArgsConstructor
public class ArticleWebService {

    private final MemberRepository memberRepository;

    public String signUp(BindingResult bindingResult, Member member) {

        if(memberRepository.findByNickname(member.getNickname()).isPresent()){
            bindingResult.reject("nicknameError","중복된 닉네임입니다.");
            return "member/addForm";
        }
        if(memberRepository.findByLoginId(member.getLoginId()).isPresent()){
            bindingResult.reject("loginIdError","중복된 아이디입니다.");
            return "member/addForm";
        }

        memberRepository.save(member);
        return "redirect:/";
    }

}
