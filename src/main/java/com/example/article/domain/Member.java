package com.example.article.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="MEMBERS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginId;

    private String password;

    private String nickname;

    private MemberLevel memberLevel;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "member")
    private List<Reply> replies = new ArrayList<>();

    public void update(String loginId,String password,String nickname){
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
    }
}
