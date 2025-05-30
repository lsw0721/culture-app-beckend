package cultureinfo.culture_app.domain;

import cultureinfo.culture_app.domain.type.*;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "member")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    @Column(unique = true)
    private String username; // 회원가입 아이디
    private String password; // 회원가입 비밀번호

    private String name; // 실제 이름
    private String email; //이메일
    private String location; // 사용자 주소
    private Long age; // 나이

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별

    @Column(unique = true)
    private String nickname; // 닉네임

    @Enumerated(EnumType.STRING)
    private Keyword1 keyword1;

    @Enumerated(EnumType.STRING)
    private Keyword2 keyword2;

    @Enumerated(EnumType.STRING)
    private Keyword3 keyword3;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = Role.class)
    @CollectionTable(
            name = "member_roles",
            joinColumns = @JoinColumn(name = "member_id")
    )
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Role> roles = new HashSet<>(); // role


    @OneToMany(mappedBy = "member") // 컨텐츠 좋아요 리스트
    private final List<ContentFavorite> contentFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "member") //
    private final List<ArticleLike> articleLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<CommentLike> commentLikes = new ArrayList<>();

    @Builder
    public Member(String username, String password, String name, String email, String location, Gender gender, String nickname, Long age, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.location = location;
        this.gender = gender;
        this.nickname = nickname;
        this.age= age;
        this.roles = roles;
    }

    public void update(String name, String nickname, String location, Gender gender, String email, Long age) {
        this.name = name;
        this.nickname = nickname;
        this.location = location;
        this.gender = gender;
        this.email = email;
        this.age = age;
    }

    //키워드 변경
    public void updateKeyword1(Keyword1 keyword1) {
        this.keyword1 = keyword1;
    }

    public void updateKeyword2(Keyword2 keyword2) {
        this.keyword2 = keyword2;
    }

    public void updateKeyword3(Keyword3 keyword3) {
        this.keyword3 = keyword3;
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
