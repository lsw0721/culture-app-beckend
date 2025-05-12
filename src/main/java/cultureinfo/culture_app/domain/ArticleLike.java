package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.*;

//중복 처리 필요//
@Entity
@Getter
@Setter
@Table(name = "article_like", uniqueConstraints = @UniqueConstraint(columnNames = {"article_id", "member_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLike {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ArticleLike(Article article, Member member) {
        this.article = article;
        this.member = member;
    }
}
