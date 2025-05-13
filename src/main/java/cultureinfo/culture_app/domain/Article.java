package cultureinfo.culture_app.domain;

import cultureinfo.culture_app.domain.type.ArticleCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    private String title; // 게시글 제목
    private String body; // 게시글 내용

    private  Long likeCount = 0L; // 좋아요 개수 처리
    private  Long commentCount = 0L; // 댓글 개수 처리

    @Enumerated(EnumType.STRING)
    private ArticleCategory category; // 게시글 종류 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "article")
    private final List<ArticleLike> articleLikes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @Builder
    public Article(String title, String body, ArticleCategory category, Member member, Content content) {
        this.title = title;
        this.body = body;
        this.category = category;
        this.member = member;
        this.content = content;
    }

    //게시글 수정
    public void update(String title, String body, ArticleCategory category) {
        this.title = title;
        this.body = body;
        this.category = category;
    }

    //좋아용 증가
    public void increaseLikeCount() {
        this.likeCount++;
    }

    //좋아요 감소
    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    //댓글 증가
    public void increaseCommentCount() {
        this.commentCount++;
    }

    //댓글 감소
    public void decreaseCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }

}
