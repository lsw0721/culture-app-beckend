package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    private String commentContent; // 댓글 내용

    private Long likeCount = 0L; // 좋아요 카운트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "comment")
    private final List<CommentLike> comments = new ArrayList<>();

    @Builder
    public Comment(String commentContent, Article article, Member member, LocalDateTime createDate, String createBy) {
        this.commentContent = commentContent;
        this.article = article;
        this.member = member;
        this.createDate = createDate;
        this.createBy = createBy;
    }

    //댓글 수정
    public void updateContent(String newContent) {
        this.commentContent = newContent;
    }

    //좋아요 증가
    public void increaseLikeCount() {
        this.likeCount++;
    }

    //좋아요 감소
    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
