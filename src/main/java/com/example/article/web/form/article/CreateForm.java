package com.example.article.web.form.article;

import com.example.article.domain.Article;
import com.example.article.domain.nonentity.ArticleCategory;
import com.example.article.domain.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class CreateForm {

    @NotEmpty
    @Size(min = 2,max = 20)
    private String title;

    @NotEmpty
    @Size(min = 2)
    private String body;

    @NotNull
    private ArticleCategory articleCategory;

    private Member member;

    private List<ArticleCategory> articleCategories;

    private List<MultipartFile> multipartFiles;

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
