package com.example.article.condition;

import lombok.Data;

@Data
public class ArticleBasicCondition {
    private String category;
    private Long memberId;

    public ArticleBasicCondition(String category, Long memberId) {
        this.category = category;
        this.memberId = memberId;
    }

    public ArticleBasicCondition(){}
}
