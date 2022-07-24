package com.example.article.web.service;

import com.example.article.api.error.BasicException;
import com.example.article.condition.article.ArticleBasicCondition;
import com.example.article.condition.article.ArticleSearchCondition;
import com.example.article.domain.*;
import com.example.article.repository.LikeRepository;
import com.example.article.repository.MemberRepository;
import com.example.article.repository.ReplyRepository;
import com.example.article.repository.article.ArticleRepository;
import com.example.article.web.dto.SimpleArticleDto;
import com.example.article.web.form.ArticleUpdateForm;
import com.example.article.web.form.CreateArticleForm;
import com.example.article.web.form.ReplyForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

import static com.example.article.api.error.BasicErrorCode.NO_ARTICLE_CONFIGURED;
import static com.example.article.api.error.BasicErrorCode.NO_MEMBER_CONFIGURED;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ArticleWebService {

    private final ReplyRepository replyRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    public Page<SimpleArticleDto> findBySearch(Pageable pageable, String selection, String searchValue,
                                               ArticleSearchCondition searchCondition, Model model) {
        searchCondition.setCondition(selection, searchValue);
        Page<SimpleArticleDto> pagedArticles = articleRepository.search(searchCondition, pageable);

        model.addAttribute("selected", selection);
        model.addAttribute("searchValue", searchValue);
        return pagedArticles;
    }

    public Page<SimpleArticleDto> findByCategory(Pageable pageable, String categoryParam,
                                                 ArticleBasicCondition categoryCondition, Model model) {
        Page<SimpleArticleDto> pagedArticles;
        pagedArticles = articleRepository.findByBasicCondition(
                categoryCondition, pageable
        );
        model.addAttribute("category", ArticleCategory.valueOf(categoryParam));
        return pagedArticles;
    }

    public void setCreateArticleForm(Long memberId, Model model) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BasicException(NO_MEMBER_CONFIGURED)
        );

        MemberLevel memberLevel = member.getMemberLevel();

        List<ArticleCategory> articleCategories = ArticleCategory.filterCategoriesByMemberLevel(memberLevel);

        model.addAttribute("articleCategories",articleCategories);
        model.addAttribute("member",member);
    }

    @Transactional
    public void save(Long memberId,CreateArticleForm createArticleForm) {
        createArticleForm.setMember(memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED)));
        Article article = createArticleForm.toEntity();
        articleRepository.save(article);
    }

    @Transactional
    public void saveReply(ReplyForm replyForm, Long memberId, Long articleId, Model model) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED));

        Article article = setBaseArticleForm(articleId, model);
        Reply reply = replyForm.toEntity(article,member);
        replyRepository.save(reply);
    }

    public Article setBaseArticleForm(Long articleId, Model model) {
        Article article = articleRepository.findWithMemberById(articleId)
                .orElseThrow(() -> new BasicException(NO_ARTICLE_CONFIGURED));

        model.addAttribute("article",article);
        return article;
    }

    public void setDetailForm(Long articleId, Model model, Long memberId) {
        Article article = setBaseArticleForm(articleId, model);

        if(memberId !=null) {
            article.addHitCount();
        }

        likeRepository.findByArticleId(articleId).ifPresent(
                likes -> model.addAttribute("likes",likes.size())
        );

        List<Reply> replies = replyRepository.findWithMemberByArticleId(articleId);

        model.addAttribute("replies",replies);
    }

    public Article setUpdateForm(Long articleId, ArticleUpdateForm updateForm) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BasicException(NO_ARTICLE_CONFIGURED));

        updateForm.setMember(article.getMember());
        return article;
    }

    @Transactional
    public void update(Long articleId,ArticleUpdateForm updateForm) {
        Article article = setUpdateForm(articleId, updateForm);
        article.update(updateForm.getTitle(), updateForm.getBody());
    }

    @Transactional
    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional
    public void saveLikes(Long articleId, Long memberId) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new BasicException(NO_ARTICLE_CONFIGURED)
        );

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED));

        likeRepository.findByMemberIdAndArticleId(memberId, articleId)
                .ifPresentOrElse(
                        //좋아요 있는 상태에서 클릭시 삭제
                        likes -> likeRepository.delete(likes),
                        //좋아요 없는 상태에서 클릭시 좋아요 생성
                        () -> likeRepository.save(Likes.builder()
                                .member(member)
                                .article(article)
                                .build())
                );
    }

}
