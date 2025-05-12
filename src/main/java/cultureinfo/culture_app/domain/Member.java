package cultureinfo.culture_app.domain;

import cultureinfo.culture_app.domain.type.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    @Column(unique = true)
    private String memberId; // 회원가입 아이디
    private String password; // 회원가입 비밀번호

    private String name; // 실제 이름
    private String email; //이메일
    private String location; // 사용자 주소

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별

    @Column(unique = true)
    private String nickname; // 닉네임

    @OneToMany(mappedBy = "member") // 컨텐츠 좋아요 리스트
    private final List<ContentFavorite> contentFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "member") //
    private final List<ArticleLike> articleLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<CommentLike> commentLikes = new ArrayList<>();

    @Builder
    public Member(String memberId, String password, String name, String email, String location, Gender gender, String nickname) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.location = location;
        this.gender = gender;
        this.nickname = nickname;
    }
}
