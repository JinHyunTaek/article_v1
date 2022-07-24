package com.example.article.condition.article;

import lombok.Data;

@Data
public class ArticleBasicCondition {
    private String category;
    private Long memberId;

    public ArticleBasicCondition(){}
}
