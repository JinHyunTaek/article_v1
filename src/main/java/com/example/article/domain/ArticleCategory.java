package com.example.article.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.article.domain.MemberLevel.*;

@RequiredArgsConstructor
@Getter
public enum ArticleCategory {

    GREET("가입 인사 게시판","모든 사용자 이용 가능"),
    FREE("자유 게시판","모든 사용자 이용 가능"),
    MARKET("장터 게시판","새내기는 이용 불가능"),
    DEBATE("토론 게시판","초보자는 이용 불가능"),
    NOTICE("공지사항 게시판","관리자만 이용 가능");

    private final String description;
    private final String message;

    public static List<ArticleCategory> filterCategoriesByMemberLevel(MemberLevel memberLevel){

        //중급자 or 상급자 -> 공지사항 외 모든 기능 사용 가능
        if(memberLevel.equals(INTERMEDIATE) || memberLevel.equals(SUPERIOR)){
            return Arrays.stream(values()).filter(articleCategory ->
                            articleCategory.equals(GREET) ||
                                    articleCategory.equals(FREE) ||
                                    articleCategory.equals(MARKET) ||
                                    articleCategory.equals(DEBATE)
                    )
                    .collect(Collectors.toList());
        }

        if(memberLevel.equals(BEGINNER)) {
            return Arrays.stream(values()).filter(articleCategory ->
                            articleCategory.equals(GREET) ||
                                    articleCategory.equals(FREE) ||
                                    articleCategory.equals(MARKET))
                    .collect(Collectors.toList());
        }

        if(memberLevel.equals(NEW)) {
            return Arrays.stream(values()).filter(articleCategory ->
                                    articleCategory.equals(GREET) || articleCategory.equals(FREE)
                    )
                    .collect(Collectors.toList());
        }

        //ADMIN 일 경우
        return List.of(ArticleCategory.values());
    }


}
