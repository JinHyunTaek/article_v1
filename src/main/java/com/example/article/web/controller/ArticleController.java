package com.example.article.web.controller;

import com.example.article.api.error.BasicException;
import com.example.article.domain.*;
import com.example.article.repository.ArticleRepository;
import com.example.article.repository.MemberRepository;
import com.example.article.repository.ReplyRepository;
import com.example.article.service.ArticleService;
import com.example.article.service.ReplyService;
import com.example.article.web.form.ArticleUpdateForm;
import com.example.article.web.form.CreateArticleForm;
import com.example.article.web.form.ReplyForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.example.article.api.error.BasicErrorCode.NO_ARTICLE_CONFIGURED;
import static com.example.article.api.error.BasicErrorCode.NO_MEMBER_CONFIGURED;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RequestMapping("/article")
@Controller
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;
    private final ReplyRepository replyRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/articles")
    public String showArticlesByCategory(
            @PageableDefault(sort = "id",direction = DESC) Pageable pageable,
            @RequestParam(name = "category") String categoryParam,
            Model model
    ){

        Page<Article> pagedArticles = articleRepository.findAllByArticleCategory(
                ArticleCategory.valueOf(categoryParam),pageable
        );
        List<Article> articles = pagedArticles.getContent();

        System.out.println("=====");

        int currentPage = pagedArticles.getPageable().getPageNumber();

        int startPage = (currentPage / 10) * 10;

        int endPage = Math.min((currentPage / 10) * 10 + 9,pagedArticles.getTotalPages());

        System.out.println("startPage:"+startPage);
        System.out.println("endPage:"+endPage);

        model.addAttribute("currentPage",currentPage);
        model.addAttribute("hasNext", pagedArticles.hasNext());
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        model.addAttribute("category",ArticleCategory.valueOf(categoryParam));
        model.addAttribute("articles",articles);
        return "article/articles";
    }

    @GetMapping("/create")
    public String createArticle(@ModelAttribute("article") Article article,
                                @SessionAttribute(name = "memberId") Long memberId,Model model){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new BasicException(NO_MEMBER_CONFIGURED));

        MemberLevel memberLevel = member.getMemberLevel();

        List<ArticleCategory> articleCategories = ArticleCategory.filterCategoriesByMemberLevel(memberLevel);

        model.addAttribute("articleCategories",articleCategories);
        model.addAttribute("member",member);
        return "article/addForm";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("article") CreateArticleForm createArticleForm,
                         BindingResult bindingResult,
                         @SessionAttribute(name = "memberId") Long memberId, Model model){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED));

        MemberLevel memberLevel = member.getMemberLevel();

        List<ArticleCategory> articleCategories = ArticleCategory.filterCategoriesByMemberLevel(memberLevel);
        model.addAttribute("member",member);
        model.addAttribute("articleCategories",articleCategories);

        if(bindingResult.hasErrors()){
            log.info("errors={}",bindingResult);
            return "article/addForm";
        }

        createArticleForm.setMember(member);

        Article article = createArticleForm.toEntity();
        articleRepository.save(article);

        return "redirect:/";
    }

    @PostMapping("/addReply")
    public String createReply(@Validated @ModelAttribute("reply") ReplyForm replyForm, BindingResult bindingResult,
                              Model model, @SessionAttribute(name = "memberId") Long memberId,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes){

        Long articleId = Long.valueOf(request.getParameter("articleId"));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BasicException(NO_ARTICLE_CONFIGURED));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED));

        model.addAttribute("article",article);
        model.addAttribute("member",member);

        if(bindingResult.hasErrors()){
            log.info("error={}",bindingResult);
            return "article/detail";
        }

        replyForm.setArticle(article);
        replyForm.setMember(member);
        Reply reply = replyForm.toEntity();
        replyRepository.save(reply);

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

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BasicException(NO_ARTICLE_CONFIGURED));

        model.addAttribute("article",article);

        List<Reply> replies = replyRepository.findByArticleId(articleId);

        model.addAttribute("replies",replies);
        return "article/detail";
    }

    @GetMapping("/update/{articleId}")
    public String update(@PathVariable Long articleId,Model model){

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BasicException(NO_ARTICLE_CONFIGURED));

        model.addAttribute("article",article);
        return "article/updateForm";
    }

    @PostMapping("/update/{articleId}")
    public String update(@PathVariable Long articleId,
                         @Valid @ModelAttribute("article") ArticleUpdateForm updateForm, BindingResult bindingResult,
                         Model model){

        Member member = articleRepository.findById(articleId)
                .orElseThrow(() -> new BasicException(NO_ARTICLE_CONFIGURED)).getMember();

        updateForm.setMember(member);

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
        articleRepository.deleteById(articleId);
    }
}
