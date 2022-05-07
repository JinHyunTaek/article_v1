package com.example.article.controller;

import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import com.example.article.dto.ArticleUpdateDto;
import com.example.article.dto.ArticleDto;
import com.example.article.dto.ReplyDto;
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
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/article")
@Controller
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleServiceImpl articleService;
    private final MemberServiceImpl memberService;
    private final ReplyServiceImpl replyService;

    @GetMapping("/create")
    public String createArticle(@ModelAttribute("article") Article article,
                                @SessionAttribute(name = "memberId") Long memberId,Model model){
        Member member = memberService.findById(memberId);
        model.addAttribute("member",member);
        return "article/addForm";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("article") ArticleDto articleDto, BindingResult bindingResult,
                         @SessionAttribute(name = "memberId") Long memberId, Model model){

        Member member = memberService.findById(memberId);
        model.addAttribute("member",member);

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "article/addForm";
        }

        articleDto.setMember(member);

        Article article = articleDto.toEntity();
        articleService.save(article);

        return "redirect:/";
    }

    @PostMapping("/addReply")
    public String createReply(@Validated @ModelAttribute("reply") ReplyDto replyDto, BindingResult bindingResult,
                              Model model, @SessionAttribute(name = "memberId") Long memberId,
                              HttpServletRequest request, RedirectAttributes redirectAttributes){

        Long articleId = Long.valueOf(request.getParameter("articleId"));

        Article article = articleService.findById(articleId);
        Member member = memberService.findById(memberId);

        model.addAttribute("article",article);
        model.addAttribute("member",member);

        if(bindingResult.hasErrors()){
            log.info("error={}",bindingResult);
            return "article/detail";
        }

        replyDto.setArticle(article);
        replyDto.setMember(member);
        Reply reply = replyDto.toEntity();
        replyService.save(reply);

        redirectAttributes.addAttribute("articleId",articleId);
        return "redirect:/article/detail/{articleId}";
    }

    @GetMapping("/detail/{articleId}")
    public String articleDetail(@PathVariable Long articleId, Model model,
                                @ModelAttribute("reply") Reply reply){
        Article article = articleService.findById(articleId);
        model.addAttribute("article",article);

        List<Reply> replies = replyService.findByArticleId(articleId);
        model.addAttribute("replies",replies);
        return "article/detail";
    }

    @GetMapping("/update/{articleId}")
    public String update(@PathVariable Long articleId,Model model,
                         @SessionAttribute("memberId") Long memberId){
        Article article = articleService.findById(articleId);
        model.addAttribute("article",article);
        return "article/updateForm";
    }

    @PostMapping("/update/{articleId}")
    public String update(@PathVariable Long articleId,
                         @Valid @ModelAttribute("article") ArticleUpdateDto updateDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "article/updateForm";
        }

        articleService.updateArticle(articleId,updateDto.getTitle(),updateDto.getBody());
        return "redirect:/article/detail/{articleId}";

    }

    @GetMapping("/delete/{articleId}")
    public String delete(@PathVariable Long articleId){
        deleteArticle(articleId);
        return "redirect:/";
    }

    @PostMapping("/delete/{articleId}")
    private void deleteArticle(@PathVariable Long articleId){
        articleService.deleteArticle(articleId);
    }
}
