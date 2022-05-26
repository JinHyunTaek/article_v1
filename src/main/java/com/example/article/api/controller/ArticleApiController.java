package com.example.article.api.controller;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.article.CreateArticleDto.CreateArticleRequest;
import com.example.article.api.dto.article.CreateArticleDto.CreateArticleResponse;
import com.example.article.api.dto.article.GetArticleDto;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleRequest;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleResponse;
import com.example.article.api.error.member.MemberErrorCode;
import com.example.article.api.error.member.MemberException;
import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.service.ArticleServiceImpl;
import com.example.article.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleApiController {

    private final ArticleServiceImpl articleService;
    private final MemberServiceImpl memberService;

    @GetMapping("/articles")
    public ResponseEntity<ApiResult<List<GetArticleDto>>> getArticles(){
        List<Article> articles = articleService.findArticlesByPageDesc();
        List<GetArticleDto> articleDtos = articles.stream()
                .map(article -> GetArticleDto.getArticleDto(article))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new ApiResult<>(articleDtos));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResult<CreateArticleResponse>> createArticle(
            @RequestBody @Valid CreateArticleRequest request
    ){

        Member member = memberService.findById(request.getMemberId());
        if(member==null){
            throw new MemberException(MemberErrorCode.NO_MEMBER_CONFIGURED);
        }

        Article article = Article.builder()
                .member(member)
                .title(request.getTitle())
                .body(request.getBody())
                .likeNumber(0)
                .hit(0)
                .build();
        articleService.save(article);
        CreateArticleResponse response = CreateArticleResponse.toDto(article);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>(response));
    }


    @GetMapping("/detail/{articleId}")
    public ResponseEntity<ApiResult<GetArticleDto>> detail(@PathVariable Long articleId){
        Article article = articleService.findById(articleId);
        GetArticleDto articleDto = GetArticleDto.getArticleDto(article);
        return ResponseEntity.ok().body(new ApiResult<>(articleDto));
    }

    @PostMapping("/update/{articleId}")
    public ResponseEntity<ApiResult<UpdateArticleResponse>> update(
            @PathVariable Long articleId,
            @Valid @RequestBody UpdateArticleRequest request){
        Article article = articleService.findById(articleId);
        articleService.updateArticle(articleId,request.getTitle(), request.getBody());

        UpdateArticleResponse updateResponse = UpdateArticleResponse.toDto(article);
        return ResponseEntity.ok().body(new ApiResult<>(updateResponse));
    }

}
