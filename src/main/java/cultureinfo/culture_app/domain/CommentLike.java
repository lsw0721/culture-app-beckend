package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
//중복처리
@Table(name = "comment_like", uniqueConstraints = @UniqueConstraint(columnNames = {"comment_id", "member_id"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class CommentLike {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public CommentLike(Comment comment, Member member) {
        this.comment = comment;
        this.member = member;
    }
}
