package com.example.article.repository.article;

import com.example.article.condition.article.ArticleBasicCondition;
import com.example.article.condition.article.ArticleSearchCondition;
import com.example.article.domain.ArticleCategory;
import com.example.article.domain.Reply;
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
import java.util.stream.Collectors;

import static com.example.article.domain.ArticleCategory.NOTICE;
import static com.example.article.domain.QArticle.article;
import static com.example.article.domain.QMember.member;
import static com.example.article.domain.QReply.reply;

public class ArticleRepositoryImpl implements ArticleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Autowired
    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<SimpleArticleDto> search(ArticleSearchCondition condition, Pageable pageable) {

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
    public Page<SimpleArticleDto> findByBasicCondition(ArticleBasicCondition condition, Pageable pageable) {

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
                        categoryEq(condition.getCategory()),
                        memberIdEq(condition.getMemberId())
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
    public Page<SimpleArticleDto> findByReplies(List<Reply> replies, Pageable pageable) {
        List<SimpleArticleDto> articles = queryFactory
                .selectDistinct(getSimpleArticleDto())
                .from(reply)
                .join(reply.article, article)
                .join(reply.member,member)
                .where(reply.in(replies))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(article.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(article.countDistinct())
                .from(reply)
                .where(reply.in(replies))
                .join(reply.article, article);
        return PageableExecutionUtils.getPage(articles,pageable,() -> countQuery.fetchOne());
    }

    @Override
    public List<SimpleArticleDto> findTop10ByCategory(ArticleCategory articleCategory) {

        return queryFactory
                .select(getSimpleArticleDtoWithCategory())
                .from(article)
                .join(article.member,member)
                .where(article.articleCategory.eq(articleCategory))
                .orderBy(article.id.desc())
                .limit(10)
                .fetch();
    }

    private JPAQuery<SimpleArticleDto> getArticleDtoSample(Pageable pageable){
        return queryFactory
                .select(getSimpleArticleDto())
                .from(article)
                .join(article.member, member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(article.id.desc());
    }

    private QSimpleArticleDto getSimpleArticleDto() {
        return new QSimpleArticleDto(
                article.id,
                article.title,
                article.replies.size(),
                member.nickname,
                article.createdDate,
                article.hit
        );
    }

    private QSimpleArticleDto getSimpleArticleDtoWithCategory() {
        return new QSimpleArticleDto(
                article.id,
                article.title,
                article.replies.size(),
                member.nickname,
                article.createdDate,
                article.hit,
                article.articleCategory
        );
    }

}
