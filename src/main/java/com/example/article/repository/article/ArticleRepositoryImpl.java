package com.example.article.repository;

import com.example.article.condition.article.ArticleBasicCondition;
import com.example.article.condition.article.ArticleSearchCondition;
import com.example.article.domain.Article;
import com.example.article.domain.ArticleCategory;
import com.example.article.web.dto.QSimpleArticleDto;
import com.example.article.web.dto.SimpleArticleDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.example.article.domain.QArticle.article;
import static com.example.article.domain.QMember.member;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Autowired
    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<SimpleArticleDto> searchArticle(ArticleSearchCondition condition, Pageable pageable) {

        List<SimpleArticleDto> articles = getArticleDtoSample(pageable)
                .where(
                        titleOrBodyContains(condition.getTitle(), condition.getBody())
                        , titleContains(condition.getTitle(), condition.getBody())
                        , bodyContains(condition.getBody(), condition.getTitle())
                        , (nicknameContains(condition.getNickname()))
                )
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article)
                .join(article.member,member)
                .where(
                        titleOrBodyContains(condition.getTitle(), condition.getBody())
                        ,titleContains(condition.getTitle(),condition.getBody())
                        ,bodyContains(condition.getBody(),condition.getTitle())
                        ,(nicknameContains(condition.getNickname()))
                );
        return PageableExecutionUtils.getPage(articles,pageable,() -> countQuery.fetchOne());
    }

    private BooleanExpression titleOrBodyContains(String title, String body){
        return StringUtils.hasText(title) && StringUtils.hasText(body) ?
                article.title.contains(title).or(article.body.contains(body)) : null;
    }

    private BooleanExpression titleContains(String title, String body) {
        return StringUtils.hasText(title) && !StringUtils.hasText(body) ? article.title.contains(title) : null;
    }

    private BooleanExpression bodyContains(String body, String title) {
        return StringUtils.hasText(body) && !StringUtils.hasText(title)? article.body.contains(body) : null;
    }

    private BooleanExpression nicknameContains(String nickname) {
        return StringUtils.hasText(nickname) ? member.nickname.contains(nickname) : null;
    }

    @Override
    public Page<SimpleArticleDto> findArticleByBasicCondition(ArticleBasicCondition condition, Pageable pageable) {

        List<SimpleArticleDto> articles = getArticleDtoSample(pageable)
                .where(
                        categoryEq(condition.getCategory()),
                        memberIdEq(condition.getMemberId())
                )
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article)
                .where(
                        categoryEq(condition.getCategory())
                );
        return PageableExecutionUtils.getPage(articles,pageable,() -> countQuery.fetchOne());
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? article.member.id.eq(memberId) : null;
    }

    private BooleanExpression categoryEq(String category) {
        System.out.println("article.articleCategory="+article.articleCategory.stringValue());
        System.out.println("category="+category);
        return StringUtils.hasText(category) ?
                article.articleCategory.stringValue().eq(category) : null;
    }

    @Override
    public Page<Article> findArticleForHome(Pageable pageable) {
        List<ArticleCategory> articleCategories = Arrays.stream(ArticleCategory.values()).toList();

        List<Article> articles = queryFactory
                .select(article)
                .from(article)
                .join(article.member,member)
                .limit(10)
                .groupBy(article.articleCategory)
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.count())
                .from(article)
                .limit(10)
                .groupBy(article.articleCategory);

        return PageableExecutionUtils.getPage(articles,pageable,() -> countQuery.fetchOne());
    }

    JPAQuery<SimpleArticleDto> getArticleDtoSample(Pageable pageable){
        return queryFactory
                .select(new QSimpleArticleDto(
                        article.id,
                        article.title,
                        article.replies.size(),
                        member.nickname,
                        article.createdDate,
                        article.hit
                ))
                .from(article)
                .join(article.member, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(article.id.desc());
    }

}
