package com.example.article.web.controller;

import com.example.article.api.error.BasicException;
import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import com.example.article.repository.ArticleRepository;
import com.example.article.repository.MemberRepository;
import com.example.article.service.MemberService;
import com.example.article.service.ReplyServiceImpl;
import com.example.article.web.form.CreateMemberForm;
import com.example.article.web.form.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.article.api.error.BasicErrorCode.NO_MEMBER_CONFIGURED;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ReplyServiceImpl replyService;

    @GetMapping("/signUp")
    public String signUp(@ModelAttribute("member") CreateMemberForm memberDto){
        return "member/addForm";
    }

    @PostMapping("/signUp")
    public String signUp(@Validated @ModelAttribute("member") CreateMemberForm memberDto, BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "member/addForm";
        }

        Member member = memberDto.toEntity();

        if(memberRepository.findByNickname(member.getNickname())!=null){
            bindingResult.reject("nicknameError","중복된 닉네임입니다.");
            return "member/addForm";
        }

        memberRepository.save(member);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("member") LoginForm loginDto){
        return "member/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("member") LoginForm loginDto, BindingResult bindingResult,
                        HttpServletRequest request,
                        @RequestParam(defaultValue = "/") String redirectURL){

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "member/loginForm";
        }

        Member loginMember = memberRepository.findByLoginIdAndPassword(loginDto.getLoginId(), loginDto.getPassword());

        if(loginMember==null){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "member/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("memberId",loginMember.getId());
        return "redirect:" +redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);//가져오되, 생성 x
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/detail/{memberId}")
    public String detail(@PathVariable("memberId") Long memberId,
                         @RequestParam(name = "replies", required = false) String repl,
                         Model model){

        if(repl!=null) {
            boolean isGetReplies = Boolean.parseBoolean(repl);
            model.addAttribute("isGetReplies", isGetReplies);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED));

        List<Article> articles = articleRepository.findByMemberIdOrderByIdDesc(memberId);
        System.out.println("=====");
        List<Reply> replies = replyService.findRepliesByMemberIdDesc(member.getId());

        List<Article> r_articles = replies.stream().map(reply -> reply.getArticle()).collect(Collectors.toList());

        model.addAttribute("member",member);
        model.addAttribute("articles",articles);
        model.addAttribute("r_articles",r_articles);

        return "member/detail";
    }

}
