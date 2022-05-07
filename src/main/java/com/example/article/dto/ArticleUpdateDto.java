package com.example.article.dto;

import com.example.article.domain.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
@Builder
@ToString
public class ArticleUpdateDto {

    @NotNull
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String body;

    @Transactional
    public void updateItemDto(Article article){
        article.builder()
                .title(this.title)
                .body(this.body)
                .build();
    }

}
