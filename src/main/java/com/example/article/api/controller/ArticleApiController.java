package com.example.article.api.controller;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.article.CreateArticleDto.CreateArticleRequest;
import com.example.article.api.dto.article.CreateArticleDto.CreateArticleResponse;
import com.example.article.api.dto.article.GetArticleDto;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleRequest;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleResponse;
import com.example.article.api.service.ArticleApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleApiController {

    private final ArticleApiService articleService;

    @GetMapping("/articles")
    public ResponseEntity<Page<ApiResult<GetArticleDto>>> getArticles(
            @PageableDefault(sort = "id",direction = DESC) Pageable pageable
    ){
        Page<ApiResult<GetArticleDto>> pagedArticleDtos = articleService.getArticles(pageable);

        return ResponseEntity
                .ok()
                .body(pagedArticleDtos);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResult<CreateArticleResponse>> createArticle(
            @RequestBody @Valid CreateArticleRequest request
    ){

        CreateArticleResponse response = articleService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>(response));
    }

    @GetMapping("/detail/{articleId}")
    public ResponseEntity<ApiResult<GetArticleDto>> detail(@PathVariable Long articleId){
        ApiResult<GetArticleDto> articleDto = articleService.detail(articleId);
        return ResponseEntity
                .ok()
                .body(articleDto);
    }

    @PostMapping("/update/{articleId}")
    public ResponseEntity<ApiResult<UpdateArticleResponse>> update(
            @PathVariable Long articleId,
            @Valid @RequestBody UpdateArticleRequest request){

        ApiResult<UpdateArticleResponse> updateResponse =
                articleService.update(articleId,request);

        return ResponseEntity.ok().body(updateResponse);
    }

}
