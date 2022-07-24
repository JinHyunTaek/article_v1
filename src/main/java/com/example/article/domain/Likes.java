package com.example.article.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class ArticleLike {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_like")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "article")
    private Article article;
}
