package com.example.article.api.dto.article;

import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetArticleDto {

    private String title;

    private String body;

    private LocalDateTime createdDateTime;

    private LocalDateTime modifiedDateTime;

    private Integer likeNumber;

    private Integer hit;

    private String nickname;

    private List<SimpleReplyDto> replies;

    public static GetArticleDto getArticleDto(Article article){
        GetArticleDto articleDto = GetArticleDto.builder()
                .title(article.getTitle())
                .body(article.getBody())
                .createdDateTime(article.getCreateDateTime())
                .modifiedDateTime(article.getModifiedDateTime())
                .likeNumber(article.getLikeNumber())
                .hit(article.getHit())
                .nickname(article.getMember().getNickname())
                .replies(article.getReplies().stream()
                        .map(reply -> new SimpleReplyDto(reply))
                        .collect(Collectors.toList()))
                .build();
        return articleDto;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SimpleReplyDto{
        private Long replyId;
        private String body;
        private String nickname;

        public SimpleReplyDto(Reply reply){
            replyId = reply.getId();
            body = reply.getBody();
            nickname = reply.getMember().getNickname();
        }
    }

}
