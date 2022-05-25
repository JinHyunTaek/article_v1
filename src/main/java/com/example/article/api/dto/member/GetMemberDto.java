package com.example.article.api.dto.member;

import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.MemberLevel;
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
public class GetMemberDto {

    private String loginId;

    private String nickname;

    private MemberLevel memberLevel;

    private List<SimpleArticleDto> articles;

    private List<SimpleReplyDto> replies;

    private LocalDateTime joinedAt;


    public static GetMemberDto toDtoWithAr(Member member){
        return GetMemberDto.builder()
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .memberLevel(member.getMemberLevel())
                .joinedAt(member.getJoinedAt())
                .articles(member.getArticles().stream()
                        .map(article -> new SimpleArticleDto(article))
                        .collect(Collectors.toList()))
                .build();
    }

    //한 명의 멤버만을 조회할 때는 in 쿼리 나가지 않음. (where articles_member_id = "", where replies_member_id = "")
    public static GetMemberDto toDtoWithArAndRep(Member member){
        return GetMemberDto.builder()
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .memberLevel(member.getMemberLevel())
                .joinedAt(member.getJoinedAt())
                .articles(member.getArticles().stream()
                        .map(article -> new SimpleArticleDto(article))
                        .collect(Collectors.toList()))
                .replies(member.getReplies().stream()
                        .map(reply -> new SimpleReplyDto(reply))
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class SimpleArticleDto{
        private Long articleId;
        private String title;
        private String body;

        public SimpleArticleDto(Article article){
            articleId = article.getId();
            title = article.getTitle();
            body = article.getBody();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SimpleReplyDto{
        private Long replyId;
        private String body;

        public SimpleReplyDto(Reply reply){
            replyId = reply.getId();
            body = reply.getBody();
        }
    }
}
