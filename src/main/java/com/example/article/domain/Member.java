package com.example.article.domain;

import com.example.article.domain.constant.Address;
import com.example.article.domain.constant.MemberLevel;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="MEMBERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = {"articles","replies","likes"})
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String loginId;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberLevel memberLevel;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<Article>();

    @OneToMany(mappedBy = "member")
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Likes> likes = new ArrayList<>();

    @Embedded
    private Address address;

    public void update(String loginId,String password,String nickname){
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
    }

    public Member(String loginId, String password, String nickname) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
    }
}
