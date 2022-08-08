package com.example.article.web.form;

import com.example.article.domain.Article;
import com.example.article.domain.File;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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

    private List<File> images;

    public static UpdateForm toForm(Article article, List<File> files){
        return UpdateForm.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .nickname(article.getMember().getNickname())
                .images(files)
                .build();
    }
}
