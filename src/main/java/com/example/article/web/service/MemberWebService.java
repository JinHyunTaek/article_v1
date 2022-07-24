package com.example.article.web.service;

import com.example.article.api.error.BasicException;
import com.example.article.condition.article.ArticleBasicCondition;
import com.example.article.domain.Member;
import com.example.article.domain.Reply;
import com.example.article.repository.MemberRepository;
import com.example.article.repository.ReplyRepository;
import com.example.article.repository.article.ArticleRepository;
import com.example.article.web.dto.SimpleArticleDto;
import com.example.article.web.form.CreateMemberForm;
import com.example.article.web.form.LoginForm;
import com.example.article.web.projections.IdAndNickname;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.article.api.error.BasicErrorCode.NO_MEMBER_CONFIGURED;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class MemberWebService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final ReplyRepository replyRepository;

    public Optional<Member> findByNickname(String nickname){
        return memberRepository.findByNickname(nickname);
    }

    public Optional<Member> findByLoginId(String loginId){
        return memberRepository.findByLoginId(loginId);
    }

    public Optional<Member> findByLoginIdAndPassword(LoginForm loginDto) {
        return memberRepository.findByLoginIdAndPassword
                (loginDto.getLoginId(), loginDto.getPassword());
    }

    @Transactional
    public void save(CreateMemberForm memberDto) {
        memberRepository.save(memberDto.toEntity());
    }

    public Page<SimpleArticleDto> getArticleDto(Long memberId, Pageable pageable) {
        ArticleBasicCondition condition = new ArticleBasicCondition();

        condition.setMemberId(memberId);
        Page<SimpleArticleDto> pagedArticles = articleRepository.findByBasicCondition(condition, pageable);
        return pagedArticles;
    }

    public Page<SimpleArticleDto> getArticleDtoOfReplies(Long memberId, Pageable pageable) {
        List<Reply> replies = replyRepository.findAllByMemberId(memberId);

        Page<SimpleArticleDto> pagedArticles = articleRepository.findByReplies(replies, pageable);
        return pagedArticles;
    }

    public IdAndNickname findIdAndNicknameById(Long memberId) {
        IdAndNickname memberCustom = memberRepository.findIdNicknameById(memberId).orElseThrow(
                () -> new BasicException(NO_MEMBER_CONFIGURED)
        );
        return memberCustom;
    }

}
