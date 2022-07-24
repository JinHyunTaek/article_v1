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
        Article article = Article.builder()
                .title(this.title)
                .body(this.body)
                .articleCategory(this.articleCategory)
                .member(member)
                .hit(0)
                .build();
        return article;
    }
}
