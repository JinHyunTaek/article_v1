package com.example.article.api.dto.reply;

import com.example.article.api.ApiResult;
import com.example.article.domain.Reply;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
public class CreateReplyDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateReplyRequest{

        @NotNull
        private Long memberId;

        @NotNull
        private Long articleId;

        @NotBlank
        @Size(min = 2, max = 50)
        private String body;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateReplyResponse{

        private Long articleId;

        private Long replyId;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        public static ApiResult<CreateReplyResponse> toDto(Reply reply){
            return new ApiResult<>(CreateReplyResponse.builder()
                    .articleId(reply.getArticle().getId())
                    .replyId(reply.getId())
                    .createdAt(reply.getCreatedDate())
                    .build());
        }

    }

}
