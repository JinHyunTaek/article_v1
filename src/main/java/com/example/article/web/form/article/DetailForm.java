package com.example.article.web.form.article;

import com.example.article.domain.Article;
import com.example.article.domain.File;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import com.example.article.web.dto.SimpleReplyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    private List<SimpleReplyDto> children;

    public static DetailForm toFormWithChildren(Article article,
                                                List<File> files,
                                                Optional<Integer> likeCount,
                                                List<SimpleReplyDto> children)
    {
        return DetailForm.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .createdDate(article.getCreatedDate())
                .member(article.getMember())
                .files(files)
                .likeCount(checkLikeCount(likeCount))
                .children(children)
                .build();
    }

    public static DetailForm toForm(Article article,
                                    List<File> files,
                                    Optional<Integer> likeCount)
    {
        return DetailForm.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .createdDate(article.getCreatedDate())
                .member(article.getMember())
                .files(files)
                .likeCount(checkLikeCount(likeCount))
                .build();
    }

    public static Integer checkLikeCount(Optional<Integer> likeCount){
        if(likeCount.isPresent()){
            return likeCount.get();
        }
        return 0;
    }
}
