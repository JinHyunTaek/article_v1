package com.example.article.web.form.article;

import com.example.article.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
@AllArgsConstructor
public class DetailForm {

    private Long id;

    private String title;

    private String body;

    private LocalDateTime createdDate;

    private Integer likeCount;

    private Member member;

    private List<Reply> replies;

    private List<File> files;

    public static DetailForm toForm(Article article,List<File> files){
        return DetailForm.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .createdDate(article.getCreatedDate())
                .member(article.getMember())
                .files(files)
                .build();
    }

    public static DetailForm toFormWithLikes(Article article, List<File> files,List<Likes> likes){
        return DetailForm.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .createdDate(article.getCreatedDate())
                .likeCount(likes.size())
                .member(article.getMember())
                .files(files)
                .build();
    }
}
