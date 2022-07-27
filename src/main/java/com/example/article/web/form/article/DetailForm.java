package com.example.article.web.form.article;

import com.example.article.domain.Article;
import com.example.article.domain.Likes;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
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

    public static DetailForm toForm(Article article,List<Reply> replies){
        return DetailForm.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .createdDate(article.getCreatedDate())
                .member(article.getMember())
                .replies(replies)
                .build();
    }

    public static DetailForm toFormWithLikes(Article article, List<Reply> replies, List<Likes> likes){
        return DetailForm.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .createdDate(article.getCreatedDate())
                .likeCount(likes.size())
                .member(article.getMember())
                .replies(replies)
                .build();
    }
}
