package com.example.article.web.controller;

import com.example.article.domain.Member;
import com.example.article.web.dto.SimpleArticleDto;
import com.example.article.web.form.CreateMemberForm;
import com.example.article.web.form.LoginForm;
import com.example.article.web.projections.IdAndNickname;
import com.example.article.web.service.MemberWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberWebService memberService;

    @GetMapping("/signUp")
    public String signUp(@ModelAttribute("member") CreateMemberForm memberDto){
        return "member/addForm";
    }

    @PostMapping("/signUp")
    public String signUp(@Validated @ModelAttribute("member") CreateMemberForm memberDto,
                         BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "member/addForm";
        }

        if(memberService.findByNickname(memberDto.getNickname()).isPresent()){
            bindingResult.reject("nicknameError","중복된 닉네임입니다.");
            return "member/addForm";
        }
        if(memberService.findByLoginId(memberDto.getLoginId()).isPresent()){
            bindingResult.reject("loginIdError","중복된 아이디입니다.");
            return "member/addForm";
        }

        memberService.save(memberDto);
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
            log.info("errors={}", bindingResult);
            return "member/loginForm";
        }

        Optional<Member> loginMember = memberService.findByLoginIdAndPassword(loginDto);

        if(loginMember.isEmpty()){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "member/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute("memberId",loginMember.get().getId());
        return "redirect:" + redirectURL;
    }


    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);//가져오되, 생성 x
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/checkLoginId")
    public String checkLoginId(@RequestParam("loginId") String loginId,
                               RedirectAttributes redirectAttributes){

        if(loginId.isBlank() || loginId.length()<3){
            redirectAttributes.addAttribute("sizeError", true);
            return "redirect:/member/signUp";
        }
        boolean isPresent = memberService.findByLoginId(loginId).isPresent();

        if(isPresent){
            redirectAttributes.addAttribute("exists", true);
            return "redirect:/member/signUp";
        }
        redirectAttributes.addAttribute("notExists", true);
        return "redirect:/member/signUp";
    }

    @GetMapping("/detail/{memberId}")
    public String detail(@PathVariable("memberId") Long memberId,
                         @PageableDefault(sort = "id",direction = DESC, size = 10) Pageable pageable,
                         Model model){
        Page<SimpleArticleDto> articleDto = memberService.getArticleDto(memberId, pageable);
        setPagingCondition(memberId,model,articleDto,articleDto.getContent());
        return "member/detail";
    }


    @GetMapping("/detail/{memberId}/replies")
    public String detailWithReplies(@PathVariable("memberId") Long memberId,
                                    @PageableDefault(sort = "id",direction = DESC, size = 10) Pageable pageable,
                                    Model model){
        Page<SimpleArticleDto> pagedArticles = memberService.getArticleDtoOfReplies(memberId, pageable);
        setPagingCondition(memberId, model, pagedArticles, pagedArticles.getContent());
        model.addAttribute("forReplies", "forReplies");
        return "member/detail";
    }

    private void setPagingCondition(Long memberId,
                                    Model model,
                                    Page<SimpleArticleDto> pagedArticles,
                                    List<SimpleArticleDto> articles) {
        IdAndNickname memberCustom = memberService.findIdAndNicknameById(memberId);

        int currentPage = pagedArticles.getPageable().getPageNumber();
        int startPage = (currentPage / 10) * 10;
        int endPage = Math.min((currentPage / 10) * 10 + 9, pagedArticles.getTotalPages());

        System.out.println("=====");

        model.addAttribute("member", memberCustom);
        model.addAttribute("articles", articles);

        model.addAttribute("currentPage",currentPage);
        model.addAttribute("hasNext", pagedArticles.hasNext());
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
    }

}
