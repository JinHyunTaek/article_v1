package com.example.article.api.service;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.article.CreateArticleDto;
import com.example.article.api.dto.article.GetArticleDto;
import com.example.article.api.dto.article.UpdateArticleDto;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleRequest;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleResponse;
import com.example.article.api.error.BasicErrorCode;
import com.example.article.api.error.BasicException;
import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.repository.ArticleRepository;
import com.example.article.repository.MemberRepository;
import com.example.article.repository.ReplyRepository;
import com.example.article.web.projections.NicknameOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.article.api.error.BasicErrorCode.NO_ARTICLE_CONFIGURED;
import static com.example.article.api.error.BasicErrorCode.NO_MEMBER_CONFIGURED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleApiService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;

    public Page<ApiResult<GetArticleDto>> getArticles(Pageable pageable) {
        Page<Article> pagedArticles = articleRepository.findAll(pageable);

        Page<ApiResult<GetArticleDto>> pagedArticleDtos = pagedArticles.map(article -> GetArticleDto.getArticleDto(article));
        return pagedArticleDtos;
    }

    public ApiResult<GetArticleDto> detail(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new BasicException(BasicErrorCode.NO_ARTICLE_CONFIGURED));

        ApiResult<GetArticleDto> articleDto = GetArticleDto.getArticleDto(article);
        return articleDto;
    }

    @Transactional
    public CreateArticleDto.CreateArticleResponse create(CreateArticleDto.CreateArticleRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BasicException(NO_MEMBER_CONFIGURED));

        Article article = Article.builder()
                .member(member)
                .title(request.getTitle())
                .body(request.getBody())
                .articleCategory(request.getArticleCategory())
                .likeNumber(0)
                .hit(0)
                .build();

        articleRepository.save(article);
        System.out.println("===");
        CreateArticleDto.CreateArticleResponse response = CreateArticleDto.CreateArticleResponse.toDto(article);
        return response;
    }

    @Transactional
    public ApiResult<UpdateArticleResponse> update(Long articleId,UpdateArticleRequest request) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BasicException(NO_ARTICLE_CONFIGURED));
        article.update(request.getTitle(), request.getBody());
        System.out.println("===");

        ApiResult<UpdateArticleResponse> updateResponse = UpdateArticleResponse.toDto(article);
        return updateResponse;
    }

}
