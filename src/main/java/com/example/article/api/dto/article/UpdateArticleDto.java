package com.example.article.api.dto.article;

import com.example.article.api.ApiResult;
import com.example.article.domain.Article;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateArticleDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateArticleRequest {

        @NotNull
        private Long id;

        @NotBlank
        @Size(min = 2, max = 10)
        private String title;

        @NotBlank
        @Size(min = 2, max = 10)
        private String body;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateArticleResponse {

        private String title;
        private String body;

        public static ApiResult<UpdateArticleResponse> toDto(Article article){
            return new ApiResult<>(UpdateArticleResponse.builder()
                    .title(article.getTitle())
                    .body(article.getBody())
                    .build());
        }
    }
}
