package com.example.article.dto;

import com.example.article.domain.Article;
import com.example.article.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class ArticleDto {

    @NotEmpty
    @Size(min = 2,max = 20)
    private String title;

    private Member member;

    @NotEmpty
    @Size(min = 2)
    private String body;

    public Article toEntity(){
        return Article.builder()
                .title(this.title)
                .likeNumber(0)
                .hit(0)
                .member(this.member)
                .createDateTime(LocalDateTime.now())
                .modifiedDateTime(LocalDateTime.now())
                .body(this.body)
                .build();
    }
}
