package com.example.article.condition.article;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.example.article.condition.article.ArticleSearchCondition.ArticleSearchConditionValue.*;

@Data
public class ArticleSearchCondition{

    private String selection;
    private String title;
    private String body;
    private String nickname;

    public ArticleSearchCondition(String title, String body, String nickname) {
        this.title = title;
        this.body = body;
        this.nickname = nickname;
    }

    public void setCondition(String selection, String searchValue){
        if(selection.equals( TITLE.getField().concat("+").concat(BODY.getField()) )){
            this.setTitle(searchValue);
            this.setBody(searchValue);
        }
        if(selection.equals(TITLE.getField())){
            this.setTitle(searchValue);
        }
        if(selection.equals(BODY.getField())){
            this.setBody(searchValue);
        }
        if(selection.equals(NICKNAME.getField())){
            this.setNickname(searchValue);
        }

    }

    @RequiredArgsConstructor
    @Getter
    public enum ArticleSearchConditionValue{

        TITLE_BODY("title+body","제목+내용"),
        TITLE("title","제목"),
        BODY("body","내용"),
        NICKNAME("nickname","작성자");

        private final String field;
        private final String description;

    }
}
