package com.example.article.domain.nonentity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberLevel {

    NEW("새내기"),
    BEGINNER("초보자"),
    INTERMEDIATE("중급자"),
    SUPERIOR("상급자"),
    ADMIN("관리자");

    private final String description;
}
