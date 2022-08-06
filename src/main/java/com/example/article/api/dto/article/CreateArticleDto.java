package com.example.article.api.dto.article;


import com.example.article.domain.Article;
import com.example.article.domain.constant.ArticleCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class CreateArticleDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateArticleRequest{

        @NotNull
        private Long memberId;

        @NotBlank
        @Size(min = 2, max = 10)
        private String title;

        @NotBlank
        @Size(min = 2, max = 10)
        private String body;

        @NotNull
        private ArticleCategory articleCategory;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateArticleResponse{

        private Long id;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        private ArticleCategory articleCategory;

        public static CreateArticleResponse toDto(Article article){
            return CreateArticleResponse.builder()
                    .id(article.getId())
                    .createdAt(article.getCreatedDate())
                    .articleCategory(article.getArticleCategory())
                    .build();
        }
    }
}
