package com.example.article.web.form;

import com.example.article.domain.Article;
import com.example.article.domain.ArticleCategory;
import com.example.article.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class CreateArticleForm {

    @NotEmpty
    @Size(min = 2,max = 20)
    private String title;

    private Member member;

    @NotEmpty
    @Size(min = 2)
    private String body;

    @NotNull
    private ArticleCategory articleCategory;

    public Article toEntity(){
        return Article.builder()
                .title(this.title)
                .member(this.member)
                .body(this.body)
                .articleCategory(this.articleCategory)
                .hit(0)
                .likeNumber(0)
                .build();
    }
}
