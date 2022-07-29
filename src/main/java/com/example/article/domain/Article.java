package com.example.article.domain;

import lombok.*;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"likes","member","replies"})
public class Article extends BaseEntity{

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ArticleCategory articleCategory;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @OneToMany(mappedBy = "article")
    private List<Likes> likes = new ArrayList<>();

    //orphanRemoval = true -> article delete 시에 orphanRemoval = true 설정된 테이블들 select 하는듯
    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer hit;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    public void update(String title, String body){
        this.title = title;
        this.body = body;
    }

    public void addHitCount(){
        this.hit += 1;
    }

    public void setMember(Member member){
        if(this.getMember()!=null){
            member.getArticles().remove(this);
        }
        this.member = member;
        member.getArticles().add(this);
    }

    public Article(String title, String body,Integer hit) {
        this.title = title;
        this.body = body;
        this.hit = hit;
    }
}
