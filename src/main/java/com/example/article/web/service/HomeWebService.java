package com.example.article.web.service;

import com.example.article.api.error.BasicException;
import com.example.article.domain.ArticleCategory;
import com.example.article.domain.Member;
import com.example.article.repository.MemberRepository;
import com.example.article.repository.article.ArticleRepository;
import com.example.article.web.dto.SimpleArticleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.article.api.error.BasicErrorCode.NO_MEMBER_CONFIGURED;
import static com.example.article.condition.article.ArticleSearchCondition.ArticleSearchConditionValue;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class HomeWebService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public void setHomeCondition(Model model) {
        List<SimpleArticleDto> articles = new ArrayList<>();
        List<ArticleCategory> articleCategories = getHomeCategories();

        model.addAttribute("selection",List.of(ArticleSearchConditionValue.values()));

        for (ArticleCategory articleCategory : articleCategories) {
            List<SimpleArticleDto> simpleArticleDtos = articleRepository.findTop10ByCategory(articleCategory);
            articles.addAll(simpleArticleDtos);
        }

        model.addAttribute("articles",articles);
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED));
    }

    private List<ArticleCategory> getHomeCategories() {
        List<ArticleCategory> categories = Arrays.stream(ArticleCategory.values()).toList();

        List<ArticleCategory> filteredCategories = categories
                .stream()
                .filter(category -> !category.name().equals("NOTICE"))
                .collect(Collectors.toList());

        System.out.println("filteredCategories = " + filteredCategories);
        return filteredCategories;
    }

}
