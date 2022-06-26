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
public class Article extends BaseEntity{

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ArticleCategory articleCategory;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Setter
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer likeNumber; //like 예약어래 야발...

    @Setter
    @Column(nullable = false, columnDefinition = "integer default 0")
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
