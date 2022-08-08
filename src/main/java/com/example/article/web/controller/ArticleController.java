package com.example.article.web.controller;

import com.example.article.condition.article.ArticleBasicCondition;
import com.example.article.condition.article.ArticleSearchCondition;
import com.example.article.condition.article.ArticleSearchCondition.ArticleSearchConditionValue;
import com.example.article.web.dto.SimpleArticleDto;
import com.example.article.web.dto.SimpleReplyDto;
import com.example.article.web.form.ReplyForm;
import com.example.article.web.form.UpdateForm;
import com.example.article.web.form.article.CreateForm;
import com.example.article.web.service.ArticleWebService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import java.net.MalformedURLException;
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

        setArticleSearchPageCond(model, pagedArticles);
        return "article/articles";
    }

    @GetMapping("/create")
    public String createArticle(@ModelAttribute("article") CreateForm form,
                                @SessionAttribute(name = "memberId") Long memberId){
        articleService.setCreateArticleForm(memberId, form);
        return "article/addForm";
    }

    @PostMapping("/create")
    public String create(@Validated @ModelAttribute("article") CreateForm form,
                         BindingResult bindingResult,
                         @SessionAttribute(name = "memberId") Long memberId){

        if(bindingResult.hasErrors()){
            articleService.setCreateArticleForm(memberId,form);
            log.info("errors={}",bindingResult);
            return "article/addForm";
        }

        articleService.save(memberId,form);

        return "redirect:/";
    }

    @PostMapping("/addReply")
    public String createReply( @PageableDefault(sort = "id",direction = DESC) Pageable pageable,
                               @RequestParam Long articleId,
                               @RequestParam(required = false) Long replyId,
                               @RequestParam(name = "parentId", required = false) Long parentId,
                               @Validated @ModelAttribute("replyForm") ReplyForm replyForm,
                               BindingResult bindingResult,
                               Model model, @SessionAttribute(name = "memberId") Long memberId,
                               RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            Page<SimpleReplyDto> replies =
                    articleService.setDetailForm(articleId, model, parentId, pageable);
            setReplyPageCond(model,replies);
            log.info("error={}",bindingResult);
            return "article/detail";
        }

        articleService.saveReply(replyForm, memberId, articleId,parentId);

        redirectAttributes.addAttribute("articleId",articleId);
        return "redirect:/article/detail";
    }

    @GetMapping("/detail")
    public String articleDetail( @PageableDefault(sort = "id",direction = DESC) Pageable pageable,
                                 @RequestParam Long articleId,
                                 @RequestParam(required = false) Long parentId,
                                 @ModelAttribute("replyForm") ReplyForm replyForm,
                                 Model model) {

        Page<SimpleReplyDto> replies = articleService.
                setDetailForm(articleId, model, parentId, pageable);
        setReplyPageCond(model,replies);
        return "article/detail";
    }

    @ResponseBody
    @GetMapping("/file/{storedFilename}")
    public Resource downloadFile(@PathVariable String storedFilename) throws MalformedURLException {
        return new UrlResource("file:"+articleService.getFullPath(storedFilename));
    }

    @PostMapping("/like/{articleId}")
    public String addLikeCount(@PathVariable Long articleId,
                               @SessionAttribute(name = "memberId") Long memberId,
                               RedirectAttributes redirectAttributes) {
        articleService.saveLikes(articleId, memberId);
        redirectAttributes.addAttribute("articleId",articleId);
        return "redirect:/article/detail";
    }

    @GetMapping("/update/{articleId}")
    public String update(@PathVariable Long articleId,
                         @SessionAttribute(name = "memberId") Long memberId,
                         Model model){
        articleService.accessValidationBySession(articleId,memberId);
        UpdateForm form = articleService.setUpdateForm(articleId);
        model.addAttribute("article",form);
        return "article/updateForm";
    }

    @PostMapping("/update/{articleId}")
    public String update(@PathVariable Long articleId,
                         @Valid @ModelAttribute("article") UpdateForm updateForm,
                         BindingResult bindingResult,
                         @RequestParam("storedFilenames") List<String> storedFilenames,
                         RedirectAttributes redirectAttributes){

        redirectAttributes.addAttribute("articleId",articleId);

        if(bindingResult.hasErrors()){
            articleService.setUpdateForm(articleId);
            log.info("errors={}",bindingResult);
            return "article/updateForm";
        }

        articleService.update(articleId,updateForm,storedFilenames);
        return "redirect:/article/detail";
    }

    @PostMapping("/delete/{articleId}")
    public String delete(@PathVariable Long articleId){
        articleService.delete(articleId);
        return "redirect:/";
    }

    private void setArticleSearchPageCond(Model model, Page<SimpleArticleDto> pagedArticles) {
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

    private void setReplyPageCond(Model model, Page<SimpleReplyDto> pagedReplies) {
        List<SimpleReplyDto> replies = pagedReplies.getContent();

        int currentPage = pagedReplies.getPageable().getPageNumber();
        int endPage = Math.min((currentPage / 10) * 10 + 9, pagedReplies.getTotalPages());

        model.addAttribute("currentPage",currentPage);
        System.out.println("currentPage = " + currentPage);
        model.addAttribute("hasPrevious",pagedReplies.hasPrevious());
        model.addAttribute("hasNext", pagedReplies.hasNext());

        model.addAttribute("replies",replies);

    }
}
