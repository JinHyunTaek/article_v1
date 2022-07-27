package com.example.article.web.form;

import com.example.article.domain.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@Builder
@ToString
public class UpdateForm {

    @NotNull
    private Long id;

    @NotEmpty
    @Size(min = 2,max = 20)
    private String title;

    @NotEmpty
    @Size(min = 2)
    private String body;

    private String nickname;

    public static UpdateForm toForm(Article article){
        return UpdateForm.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .nickname(article.getMember().getNickname())
                .build();
    }
}
