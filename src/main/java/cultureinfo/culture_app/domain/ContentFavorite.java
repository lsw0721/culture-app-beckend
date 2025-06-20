package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "content_detail_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//콘텐츠 찜
public class ContentFavorite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_detail_id")
    private ContentDetail contentDetail;

    @CreationTimestamp
    private LocalDateTime createdAt;
        
    @Builder
    public ContentFavorite(Member member, ContentDetail contentDetail) {
        this.member = member;
        this.contentDetail = contentDetail;
    }
}
