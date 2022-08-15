package com.example.article.api.controller;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.article.CreateArticleDto.CreateArticleRequest;
import com.example.article.api.dto.article.CreateArticleDto.CreateArticleResponse;
import com.example.article.api.dto.article.ApiArticleDto;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleRequest;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleResponse;
import com.example.article.api.service.ArticleApiService;
import com.example.article.condition.article.ArticleSearchCondition;
import com.example.article.domain.constant.ArticleCategory;
import com.example.article.web.dto.SimpleArticleDto;
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
    public ResponseEntity<Page<ApiResult<ApiArticleDto>>> getArticles(
            @PageableDefault(sort = "id",direction = DESC) Pageable pageable,
            @RequestParam(name = "category", required = false) ArticleCategory articleCategory
            ){
        if(articleCategory != null){
            Page<ApiResult<ApiArticleDto>> byCategories =
                    articleService.findByCategory(articleCategory, pageable);

            return ResponseEntity
                    .ok()
                    .body(byCategories);
        }

        Page<ApiResult<ApiArticleDto>> dtos = articleService.getArticles(pageable);

        return ResponseEntity
                .ok()
                .body(dtos);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResult<CreateArticleResponse>> createArticle(
             @Valid CreateArticleRequest request
            ){
        CreateArticleResponse response = articleService.create(request,request.getMultipartFiles());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>(response));
    }

    @GetMapping("/detail/{articleId}")
    public ResponseEntity<ApiResult<ApiArticleDto>> detail(@PathVariable Long articleId){
        ApiResult<ApiArticleDto> articleDto = articleService.detail(articleId);
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

    @PostMapping("/delete/{articleId}")
    public ResponseEntity<ApiResult<String>> delete(
            @PathVariable Long articleId
    ){
        articleService.delete(articleId);
        return ResponseEntity.ok().body(new ApiResult<>("removed"));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<SimpleArticleDto>> search(
            ArticleSearchCondition condition,
            @PageableDefault(sort = "id",direction = DESC) Pageable pageable
    ){
        Page<SimpleArticleDto> response = articleService.findBySearch(condition, pageable);
        return ResponseEntity.ok()
                .body(response);
    }

}
