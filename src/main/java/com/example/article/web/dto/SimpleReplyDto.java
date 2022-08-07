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

    private Long parentId;

    public static SimpleReplyDto toDto(Reply reply){
        return SimpleReplyDto.builder()
                .id(reply.getId())
                .body(reply.getBody())
                .nickname(reply.getMember().getNickname())
                .createdDate(reply.getCreatedDate())
                .build();
    }

    public static SimpleReplyDto toDtoWithParent(Reply reply){
        return SimpleReplyDto.builder()
                .id(reply.getId())
                .body(reply.getBody())
                .nickname(reply.getMember().getNickname())
                .createdDate(reply.getCreatedDate())
                .parentId(reply.getParent().getId())
                .build();
    }

    protected SimpleReplyDto(Long id, String body, String nickname,
                             LocalDateTime createdDate, Long parentId) {
        this.id = id;
        this.body = body;
        this.nickname = nickname;
        this.createdDate = createdDate;
        this.parentId = parentId;
    }
}
