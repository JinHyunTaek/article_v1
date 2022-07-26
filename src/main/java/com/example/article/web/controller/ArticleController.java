package com.example.article.web.controller;

import com.example.article.condition.article.ArticleBasicCondition;
import com.example.article.condition.article.ArticleSearchCondition;
import com.example.article.condition.article.ArticleSearchCondition.ArticleSearchConditionValue;
import com.example.article.domain.Article;
import com.example.article.domain.Reply;
import com.example.article.web.dto.SimpleArticleDto;
import com.example.article.web.form.ArticleUpdateForm;
import com.example.article.web.form.CreateArticleForm;
import com.example.article.web.form.ReplyForm;
import com.example.article.web.service.ArticleWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RequestMapping("/article")
@Controller
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleWebService articleService;

    @GetMapping("/articles")
    public String showArticles(
            @PageableDefault(sort = "id",direction = DESC) Pageable pageable,
            @RequestParam(name = "category", required = false) String categoryParam,
            @RequestParam(name = "selection", required = false) String selection,
            @RequestParam(name = "searchValue", required = false) String searchValue,
            @ModelAttribute("searchCondition") ArticleSearchCondition searchCondition,
            @ModelAttribute("categoryCondition") ArticleBasicCondition categoryCondition,
            Model model
    ){
        Page<SimpleArticleDto> pagedArticles;

        //검색
        if(StringUtils.hasText(selection) || StringUtils.hasText(searchValue)){
            pagedArticles = articleService.findBySearch(pageable, selection, searchValue, searchCondition, model);
        }

        //더보기
        else{
            pagedArticles = articleService.findByCategory(pageable, categoryParam, categoryCondition, model);
        }

        setPagingCondition(model, pagedArticles);
        return "article/articles";
    }

    @GetMapping("/create")
    public String createArticle(@ModelAttribute("article") Article article,
                                @SessionAttribute(name = "memberId") Long memberId,Model model){
        articleService.setCreateArticleForm(memberId, model);
        return "article/addForm";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("article") CreateArticleForm createArticleForm,
                         BindingResult bindingResult,
                         @SessionAttribute(name = "memberId") Long memberId, Model model){

        if(bindingResult.hasErrors()){
            articleService.setCreateArticleForm(memberId,model);
            log.info("errors={}",bindingResult);
            return "article/addForm";
        }

        articleService.save(memberId,createArticleForm);

        return "redirect:/";
    }

    @PostMapping("/addReply/{articleId}")
    public String createReply(@PathVariable Long articleId,
                              @Validated @ModelAttribute("reply") ReplyForm replyForm,
                              BindingResult bindingResult,
                              Model model, @SessionAttribute(name = "memberId") Long memberId,
                              RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            articleService.setBaseArticleForm(articleId, model);
            log.info("error={}",bindingResult);
            return "article/detail";
        }

        articleService.saveReply(replyForm, memberId, articleId, model);

        redirectAttributes.addAttribute("articleId",articleId);
        return "redirect:/article/detail/{articleId}";
    }

    @GetMapping("/detail/{articleId}")
    public String articleDetail(@PathVariable Long articleId, Model model,
                                @ModelAttribute("reply") Reply reply,
                                @SessionAttribute(name = "memberId", required = false) Long memberId) {

        articleService.setDetailForm(articleId, model, memberId);
        return "article/detail";
    }

    @PostMapping("/like/{articleId}")
    public String addLikeCount(@PathVariable Long articleId,
                               @SessionAttribute(name = "memberId") Long memberId) {
        articleService.saveLikes(articleId, memberId);
        return "redirect:/article/detail/{articleId}";
    }

    @GetMapping("/update/{articleId}")
    public String update(@PathVariable Long articleId,
                         @SessionAttribute(name = "memberId") Long memberId,
                         Model model){
        articleService.setBaseArticleForm(articleId,model);
        return "article/updateForm";
    }

    @PostMapping("/update/{articleId}")
    public String update(@PathVariable Long articleId,
                         @Valid @ModelAttribute("article") ArticleUpdateForm updateForm,
                         BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            articleService.setUpdateForm(articleId, updateForm);
            log.info("errors={}",bindingResult);
            return "article/updateForm";
        }

        articleService.update(articleId,updateForm);
        return "redirect:/article/detail/{articleId}";
    }

    @PostMapping("/delete/{articleId}")
    public String delete(@PathVariable Long articleId){
        articleService.delete(articleId);
        return "redirect:/";
    }

    private void setPagingCondition(Model model, Page<SimpleArticleDto> pagedArticles) {
        List<SimpleArticleDto> articles = pagedArticles.getContent();

        int currentPage = pagedArticles.getPageable().getPageNumber();
        int startPage = (currentPage / 10) * 10;
        int endPage = Math.min((currentPage / 10) * 10 + 9, pagedArticles.getTotalPages());

        model.addAttribute("currentPage",currentPage);
        model.addAttribute("hasNext", pagedArticles.hasNext());
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        model.addAttribute("selection",List.of(ArticleSearchConditionValue.values()));
        model.addAttribute("articles",articles);
    }
}
