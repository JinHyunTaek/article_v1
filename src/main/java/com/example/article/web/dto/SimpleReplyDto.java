package com.example.article.web.dto;

import com.example.article.domain.Reply;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class SimpleReplyDto {

    private Long id;

    private String body;

    private String nickname;

    private LocalDateTime createdDate;

    public static SimpleReplyDto toDto(Reply reply){
        return SimpleReplyDto.builder()
                .id(reply.getId())
                .body(reply.getBody())
                .nickname(reply.getMember().getNickname())
                .createdDate(reply.getCreatedDate())
                .build();
    }

    public SimpleReplyDto(Long id, String body, String nickname, LocalDateTime createdDate) {
        this.id = id;
        this.body = body;
        this.nickname = nickname;
        this.createdDate = createdDate;
    }

}
