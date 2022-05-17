package com.example.article.web.controller;

import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import com.example.article.web.form.ArticleUpdateForm;
import com.example.article.web.form.CreateArticleForm;
import com.example.article.web.form.ReplyForm;
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
    public String create(@Validated @ModelAttribute("article") CreateArticleForm createArticleForm, BindingResult bindingResult,
                         @SessionAttribute(name = "memberId") Long memberId, Model model){

        Member member = memberService.findById(memberId);
        model.addAttribute("member",member);

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "article/addForm";
        }

        createArticleForm.setMember(member);

        Article article = createArticleForm.toEntity();
        articleService.save(article);

        return "redirect:/";
    }

    @PostMapping("/addReply")
    public String createReply(@Validated @ModelAttribute("reply") ReplyForm replyForm, BindingResult bindingResult,
                              Model model, @SessionAttribute(name = "memberId") Long memberId,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes){

        Long articleId = Long.valueOf(request.getParameter("articleId"));

        Article article = articleService.findById(articleId);
        Member member = memberService.findById(memberId);

        model.addAttribute("article",article);
        model.addAttribute("member",member);

        if(bindingResult.hasErrors()){
            log.info("error={}",bindingResult);
            return "article/detail";
        }

        replyForm.setArticle(article);
        replyForm.setMember(member);
        Reply reply = replyForm.toEntity();
        replyService.save(reply);

        redirectAttributes.addAttribute("articleId",articleId);
        return "redirect:/article/detail/{articleId}";
    }

    @GetMapping("/detail/{articleId}")
    public String articleDetail(@PathVariable Long articleId, Model model,
                                @ModelAttribute("reply") Reply reply,
                                @SessionAttribute(name = "memberId", required = false) Long memberId){

        if(memberId!=null) {
            articleService.addHitCount(articleId);
        }

        Article article = articleService.findById(articleId);

        model.addAttribute("article",article);

        List<Reply> replies = replyService.findByArticleId(articleId);
        model.addAttribute("replies",replies);
        return "article/detail";
    }

    @GetMapping("/update/{articleId}")
    public String update(@PathVariable Long articleId,Model model){
        Article article = articleService.findById(articleId);
        model.addAttribute("article",article);
        return "article/updateForm";
    }

    @PostMapping("/update/{articleId}")
    public String update(@PathVariable Long articleId,
                         @Valid @ModelAttribute("article") ArticleUpdateForm updateForm, BindingResult bindingResult,
                         Model model){

        updateForm.setMember(articleService.findById(articleId).getMember());

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "article/updateForm";
        }

        articleService.updateArticle(articleId,updateForm.getTitle(),updateForm.getBody());
        return "redirect:/article/detail/{articleId}";
    }

    @GetMapping("/search")
    public String getSearchValueList(
            @RequestParam("selection") String selection,
            @RequestParam("searchValue") String searchValue
    ){
        return "redirect:/";
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
