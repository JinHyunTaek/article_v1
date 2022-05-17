package com.example.article.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "createdAt")
    private LocalDateTime createDateTime;

    @Column(name = "modifiedAt")
    private LocalDateTime modifiedDateTime;

    @Column(columnDefinition = "integer default 0")
    private Integer likeNumber; //like 예약어래 야발...

    @Column(columnDefinition = "integer default 0")
    private Integer hit;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    public void update(String title, String body){
        this.title = title;
        this.body = body;
    }

    public void addHitCount(){
        this.hit += 1;
    }
}
