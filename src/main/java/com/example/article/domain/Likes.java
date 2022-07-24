package com.example.article.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class Likes {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_like")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article")
    private Article article;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member")
    private Member member;

    public Likes(Article article, Member member) {
        this.article = article;
        this.member = member;
    }

}
