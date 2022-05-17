package com.example.article.web.controller;

import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import com.example.article.web.form.CreateMemberForm;
import com.example.article.web.form.LoginForm;
import com.example.article.service.ArticleServiceImpl;
import com.example.article.service.MemberServiceImpl;
import com.example.article.service.ReplyServiceImpl;
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

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberServiceImpl memberService;
    private final ArticleServiceImpl articleService;
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

        if(memberService.findByNickname(member.getNickname())!=null){
            bindingResult.reject("nicknameError","중복된 닉네임입니다.");
            return "member/addForm";
        }

        memberService.saveMember(member);
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

        Member loginMember = memberService.login(loginDto.getLoginId(), loginDto.getPassword());

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

        Member member = memberService.findById(memberId);

        List<Article> articles = articleService.findArticlesByMemberIdDesc(member.getId());
        List<Reply> replies = replyService.findRepliesByMemberIdDesc(member.getId());

        List<Article> r_articles = replies.stream().map(reply -> reply.getArticle()).collect(Collectors.toList());

        model.addAttribute("member",member);
        model.addAttribute("articles",articles);
        model.addAttribute("r_articles",r_articles);

        return "member/detail";
    }

}
