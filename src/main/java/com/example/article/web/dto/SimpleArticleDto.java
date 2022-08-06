package com.example.article.web.dto;

import com.example.article.domain.constant.ArticleCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class SimpleArticleDto {

    private Long articleId;

    private String title;

    private Integer replyCount;

    private String nickname;

    private LocalDateTime createdAt;

    private Integer hit;

    private ArticleCategory articleCategory;

    @QueryProjection
    public SimpleArticleDto(Long articleId, String title, Integer replyCount, String nickname, LocalDateTime createdAt, Integer hit) {
        this.articleId = articleId;
        this.title = title;
        this.replyCount = replyCount;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.hit = hit;
    }

    @QueryProjection
    public SimpleArticleDto(Long articleId, String title, Integer replyCount,
                            String nickname, LocalDateTime createdAt,
                            Integer hit, ArticleCategory articleCategory) {
        this.articleId = articleId;
        this.title = title;
        this.replyCount = replyCount;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.hit = hit;
        this.articleCategory = articleCategory;
    }
}
