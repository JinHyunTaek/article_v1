package com.example.article.api.controller;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.article.CreateArticleDto.CreateArticleRequest;
import com.example.article.api.dto.article.CreateArticleDto.CreateArticleResponse;
import com.example.article.api.dto.article.GetArticleDto;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleRequest;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleResponse;
import com.example.article.api.error.BasicErrorCode;
import com.example.article.api.error.BasicException;
import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.repository.ArticleRepository;
import com.example.article.service.ArticleService;
import com.example.article.service.MemberService;
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

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;
    private final MemberService memberService;

    @GetMapping("/articles")
    public ResponseEntity<Page<ApiResult<GetArticleDto>>> getArticles(
            @PageableDefault(sort = "id",direction = DESC) Pageable pageable
    ){
        Page<Article> pagedArticles = articleRepository.findAll(pageable);

//        List<Article> articles = pagedArticles.getContent();
//        List<ApiResult<GetArticleDto>> articleDtos = pagedArticles.stream()
//                .map(article -> GetArticleDto.getArticleDto(article))
//                .collect(Collectors.toList());

        Page<ApiResult<GetArticleDto>> pagedArticleDtos = pagedArticles.map(article -> GetArticleDto.getArticleDto(article));

        return ResponseEntity
                .ok()
                .body(pagedArticleDtos);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResult<CreateArticleResponse>> createArticle(
            @RequestBody @Valid CreateArticleRequest request
    ){

        Member member = memberService.findById(request.getMemberId());
        if(member==null){
            throw new BasicException(BasicErrorCode.NO_MEMBER_CONFIGURED);
        }

        Article article = Article.builder()
                .member(member)
                .title(request.getTitle())
                .body(request.getBody())
                .articleCategory(request.getArticleCategory())
                .likeNumber(0)
                .hit(0)
                .build();

        articleRepository.save(article);
        CreateArticleResponse response = CreateArticleResponse.toDto(article);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>(response));
    }


    @GetMapping("/detail/{articleId}")
    public ResponseEntity<ApiResult<GetArticleDto>> detail(@PathVariable Long articleId){
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new BasicException(BasicErrorCode.NO_ARTICLE_CONFIGURED));

        ApiResult<GetArticleDto> articleDto = GetArticleDto.getArticleDto(article);
        return ResponseEntity
                .ok()
                .body(articleDto);
    }

    @PostMapping("/update/{articleId}")
    public ResponseEntity<ApiResult<UpdateArticleResponse>> update(
            @PathVariable Long articleId,
            @Valid @RequestBody UpdateArticleRequest request){
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new BasicException(BasicErrorCode.NO_ARTICLE_CONFIGURED));

        articleService.updateArticle(articleId, request.getTitle(), request.getBody());

        ApiResult<UpdateArticleResponse> updateResponse = UpdateArticleResponse.toDto(article);
        return ResponseEntity.ok().body(updateResponse);
    }

}
