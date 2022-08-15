package com.example.article.web.service;

import com.example.article.api.error.BasicException;
import com.example.article.condition.article.ArticleBasicCondition;
import com.example.article.condition.article.ArticleSearchCondition;
import com.example.article.domain.*;
import com.example.article.domain.constant.ArticleCategory;
import com.example.article.domain.constant.MemberLevel;
import com.example.article.repository.FileRepository;
import com.example.article.repository.LikeRepository;
import com.example.article.repository.MemberRepository;
import com.example.article.repository.ReplyRepository;
import com.example.article.repository.article.ArticleRepository;
import com.example.article.web.dto.SimpleArticleDto;
import com.example.article.web.dto.SimpleReplyDto;
import com.example.article.web.form.ReplyForm;
import com.example.article.web.form.UpdateForm;
import com.example.article.web.form.article.CreateForm;
import com.example.article.web.form.article.DetailForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.article.api.error.BasicErrorCode.*;
import static com.example.article.web.form.UpdateForm.toForm;
import static com.example.article.web.form.article.DetailForm.*;
import static com.example.article.web.form.article.DetailForm.toForm;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ArticleWebService {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName){
        return fileDir+fileName;
    }

    private final ReplyRepository replyRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final FileRepository fileRepository;

    public Page<SimpleArticleDto> findBySearch(Pageable pageable, String selection, String searchValue,
                                               ArticleSearchCondition searchCondition, Model model) {
        searchCondition.setCondition(selection, searchValue);
        Page<SimpleArticleDto> pagedArticles = articleRepository.search(searchCondition, pageable);

        model.addAttribute("selected", selection);
        model.addAttribute("searchValue", searchValue);
        return pagedArticles;
    }

    public Page<SimpleArticleDto> findByCategory(Pageable pageable, String categoryParam,
                                                 ArticleBasicCondition categoryCondition, Model model) {
        Page<SimpleArticleDto> pagedArticles;
        pagedArticles = articleRepository.findByBasicCondition(
                categoryCondition, pageable
        );
        model.addAttribute("category", ArticleCategory.valueOf(categoryParam));
        return pagedArticles;
    }

    public void setCreateArticleForm(Long memberId, CreateForm form) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BasicException(MEMBER_NOT_FOUND)
        );
        MemberLevel memberLevel = member.getMemberLevel();

        List<ArticleCategory> articleCategories = ArticleCategory.filterCategoriesByMemberLevel(memberLevel);
        form.setArticleCategories(articleCategories);
        form.setMember(member);
    }

    @Transactional
    public void saveReply(ReplyForm replyForm, Long memberId, Long articleId, Long parentId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(MEMBER_NOT_FOUND));

        Article article = articleRepository.findWithMemberById(articleId)
                .orElseThrow(() -> new BasicException(ARTICLE_NOT_FOUND));

        if (parentId != null) {
            Reply parent = replyRepository.findById(parentId).orElseThrow(
                    () -> new BasicException(REPLY_NOT_FOUND));
            Reply reply = replyForm.toEntityByParentReply(article, member, parent);
            replyRepository.save(reply);
        } else {
            com.example.article.domain.Reply reply = replyForm.toEntity(article, member);
            replyRepository.save(reply);
        }
    }

    public Page<SimpleReplyDto> setDetailForm(Long articleId, Model model,
                                              Long parentId,Pageable pageable) {

        Article article = articleRepository.findWithMemberById(articleId)
                .orElseThrow(() -> new BasicException(ARTICLE_NOT_FOUND));

        Page<Reply> pagedReplies = replyRepository.findByArticleId(articleId, pageable);

        Page<SimpleReplyDto> replies = pagedReplies.map(SimpleReplyDto::toDto);

        List<File> files = fileRepository.findByArticleId(articleId);

        Optional<Integer> likeCount = likeRepository.countByArticleId(articleId);

        if(parentId != null){
            List<Reply> children = replyRepository.findByParentId(parentId);
            List<SimpleReplyDto> childrenDto = children.stream()
                    .map(SimpleReplyDto::toDtoWithParent).collect(Collectors.toList());
            DetailForm form = toFormWithChildren(article, files, likeCount, childrenDto);
            model.addAttribute("article",form);
            model.addAttribute("parentId",parentId);
            return replies;
        }

        DetailForm form = toForm(article, files, likeCount);
        model.addAttribute("article",form);

        return replies;
    }

    public UpdateForm setUpdateForm(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BasicException(ARTICLE_NOT_FOUND));
        List<File> files = fileRepository.findByArticleId(articleId);
        UpdateForm form = toForm(article,files);
        return form;
    }

    @Transactional
    public void update(Long articleId, UpdateForm updateForm,List<String> storedFilenames) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BasicException(ARTICLE_NOT_FOUND));
        article.update(updateForm.getTitle(), updateForm.getBody());
        if(updateForm.getNewImages()!=null){
            System.out.println("updateForm = " + updateForm.getNewImages());
            saveFiles(updateForm.getNewImages(),article);
        }
        if(storedFilenames != null)
            for (String storedFilename : storedFilenames) {
                fileRepository.deleteByStoredFilename(storedFilename);
                java.io.File io_file = new java.io.File(getFullPath(storedFilename));
                io_file.delete();
            }

    }

    @Transactional
    public void delete(Long articleId) {
        List<File> files = fileRepository.findByArticleId(articleId);
        if(files.isEmpty()) {
            articleRepository.deleteById(articleId);
            return;
        }
        deleteFilesInDirectory(files);
        articleRepository.deleteById(articleId);
    }

    private void deleteFilesInDirectory(List<File> files) {
        for (File file : files) {
            String storedFilename = file.getStoredFilename();
            java.io.File io_file = new java.io.File(getFullPath(storedFilename));
            io_file.delete();
        }
    }

    @Transactional
    public void saveLikes(Long articleId, Long memberId) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new BasicException(ARTICLE_NOT_FOUND)
        );

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(MEMBER_NOT_FOUND));

        likeRepository.findByMemberIdAndArticleId(memberId, articleId)
                .ifPresentOrElse(
                        //좋아요 있는 상태에서 클릭시 삭제
                        likes -> likeRepository.delete(likes),
                        //좋아요 없는 상태에서 클릭시 좋아요 생성
                        () -> likeRepository.save(Likes.builder()
                                .member(member)
                                .article(article)
                                .build())
                );
    }

    public void accessValidationBySession(Long articleId, Long memberId){
        articleRepository.findByIdAndMemberId(articleId,memberId).ifPresentOrElse(
                member -> {
                    return;
                },
                () -> {
                    System.out.println("memberId = " + memberId);
                    throw new BasicException(FORBIDDEN);
                });
    }

    @Transactional
    public void save(Long memberId,CreateForm form) {
        form.setMember(memberRepository.findById(memberId)
                .orElseThrow(() -> new BasicException(MEMBER_NOT_FOUND)));
        Article article = form.toEntity();
        articleRepository.save(article);
        saveFiles(form.getImageFiles(),article);
    }

    @Transactional
    public void saveFiles(List<MultipartFile> multipartFiles, Article article) {
        List<File> storeFiles = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if(multipartFile.isEmpty()) return;
            File file = saveFile(multipartFile, article);
            fileRepository.save(file);
            storeFiles.add(file);
        }
    }

    @Transactional
    public File saveFile(MultipartFile multipartFile,Article article)  {
        if(multipartFile.isEmpty()){
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        String fullPath = getFullPath(storeFileName);

        try{
            multipartFile.transferTo(new java.io.File(fullPath));
        }catch (IOException e){
            throw new BasicException(INTERNAL_SERVER_ERROR,e);
        }

        return File.builder()
                .article(article)
                .originalFilename(originalFilename)
                .storedFilename(storeFileName)
                .fileDirectory(fullPath)
                .build();
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);
        return ext;
    }

}
