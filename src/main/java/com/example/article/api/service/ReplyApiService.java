package com.example.article.api.service;

import com.example.article.api.ApiResult;
import com.example.article.api.dto.reply.CreateReplyDto;
import com.example.article.api.dto.reply.CreateReplyDto.CreateReplyResponse;
import com.example.article.api.error.BasicException;
import com.example.article.domain.Article;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import com.example.article.repository.MemberRepository;
import com.example.article.repository.ReplyRepository;
import com.example.article.repository.article.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import static com.example.article.api.dto.reply.CreateReplyDto.*;
import static com.example.article.api.error.BasicErrorCode.ARTICLE_NOT_FOUND;
import static com.example.article.api.error.BasicErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyApiService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public ApiResult<CreateReplyResponse> save(
            CreateReplyRequest request
    ){

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BasicException(MEMBER_NOT_FOUND));

        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new BasicException(ARTICLE_NOT_FOUND));

        Reply reply = Reply.builder()
                .member(member)
                .article(article)
                .body(request.getBody())
                .build();

        replyRepository.save(reply);

        return CreateReplyResponse.toDto(reply);

    }

}
