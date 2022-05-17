package com.example.article.web.form;

import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class ReplyForm {

    private Member member;

    private Article article;

    @NotEmpty
    @Size(min = 2)
    private String body;

    public Reply toEntity(){
        return Reply.builder()
                .article(this.article)
                .member(this.member)
                .body(this.body)
                .likeNumber(0)
                .build();
    }
}
