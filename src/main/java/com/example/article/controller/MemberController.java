package com.example.article.controller;

import com.example.article.domain.Member;
import com.example.article.dto.CreateMemberDto;
import com.example.article.dto.LoginDto;
import com.example.article.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberServiceImpl memberService;

    @GetMapping("/signUp")
    public String signUp(@ModelAttribute("member") CreateMemberDto memberDto){
        return "member/addForm";
    }

    @PostMapping("/signUp")
    public String signUp(@Validated @ModelAttribute("member") CreateMemberDto memberDto, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "member/addForm";
        }

        Member member = memberDto.toEntity();
        memberService.saveMember(member);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("member")LoginDto loginDto){
        return "member/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("member")LoginDto loginDto, BindingResult bindingResult,
                        HttpServletRequest request){

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "member/loginForm";
        }

        Member loginMember = memberService.login(loginDto.getLoginId(), loginDto.getPassword());

        if(loginMember==null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "member/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("memberId",loginMember.getId());
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);//가져오되, 생성 x
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }
}
