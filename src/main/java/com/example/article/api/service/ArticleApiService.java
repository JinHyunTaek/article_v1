package com.example.article.api.service;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.article.CreateArticleDto;
import com.example.article.api.dto.article.CreateArticleDto.CreateArticleRequest;
import com.example.article.api.dto.article.ApiArticleDto;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleRequest;
import com.example.article.api.dto.article.UpdateArticleDto.UpdateArticleResponse;
import com.example.article.api.error.BasicErrorCode;
import com.example.article.api.error.BasicException;
import com.example.article.condition.article.ArticleSearchCondition;
import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.constant.ArticleCategory;
import com.example.article.repository.FileRepository;
import com.example.article.repository.article.ArticleRepository;
import com.example.article.repository.MemberRepository;
import com.example.article.repository.ReplyRepository;
import com.example.article.web.dto.SimpleArticleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.article.api.error.BasicErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleApiService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final FileRepository fileRepository;

    private final FileApiService fileService;

    public Page<ApiResult<ApiArticleDto>> getArticles(Pageable pageable) {
        Page<Article> pagedArticles = articleRepository.findAll(pageable);

        Page<ApiResult<ApiArticleDto>> pagedArticleDtos = pagedArticles.map(article -> ApiArticleDto.getArticleDto(article));
        return pagedArticleDtos;
    }

    public ApiResult<ApiArticleDto> detail(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new BasicException(BasicErrorCode.ARTICLE_NOT_FOUND));

        ApiResult<ApiArticleDto> articleDto = ApiArticleDto.getArticleDto(article);
        return articleDto;
    }

    @Transactional
    public CreateArticleDto.CreateArticleResponse create(
            CreateArticleRequest request,
            List<MultipartFile> multipartFiles)
    {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BasicException(MEMBER_NOT_FOUND));

        Article article = Article.builder()
                .member(member)
                .title(request.getTitle())
                .body(request.getBody())
                .articleCategory(request.getArticleCategory())
                .hit(0)
                .build();

        articleRepository.save(article);
        fileService.saveFiles(multipartFiles,article);
        System.out.println("===");
        CreateArticleDto.CreateArticleResponse response = CreateArticleDto.CreateArticleResponse.toDto(article);
        return response;
    }

    @Transactional
    public ApiResult<UpdateArticleResponse> update(Long articleId,UpdateArticleRequest request) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BasicException(ARTICLE_NOT_FOUND));
        article.update(request.getTitle(), request.getBody());
        System.out.println("===");

        ApiResult<UpdateArticleResponse> updateResponse = UpdateArticleResponse.toDto(article);
        return updateResponse;
    }

    @Transactional
    public void delete(Long articleId) {

        Article article = articleRepository.findById(articleId)
                .orElseThrow(()->new BasicException(ARTICLE_NOT_FOUND));

        try {
            if(!article.getFiles().isEmpty()){
                fileService.deleteFilesInDirectory(article.getFiles());
            }
        }catch (Exception e){
            throw new BasicException(DATA_ACCESS_ERROR,e);
        }

        try {
            articleRepository.delete(article);
        }catch (Exception e){
            throw new BasicException(DATA_ACCESS_ERROR,e);
        }

    }

    public Page<ApiResult<ApiArticleDto>> findByCategory(ArticleCategory category, Pageable pageable){
        Page<Article> articles = articleRepository.findByArticleCategory(category, pageable);
        Page<ApiResult<ApiArticleDto>> pagedArticleDtos = articles.map(
                article -> ApiArticleDto.getArticleDto(article)
        );
        return pagedArticleDtos;
    }

    public Page<SimpleArticleDto> findBySearch(ArticleSearchCondition condition, Pageable pageable){
        return articleRepository.search(condition, pageable);
    }

}
