package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Content {
    @Id @GeneratedValue
    private Long id;

    private String contentName; // 콘텐츠 이름
    private LocalDateTime startDateTime; // 시작일자
    private LocalDateTime endDateTime; // 종료일자
    private String location; // 콘텐츠 지역
    private Long price = 0L; // 콘텐츠 가격
    private String picture; // 콘텐츠 사진 - URL 경로 설계 등 필요

    @Lob
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentSubcategory_id")
    private ContentSubcategory contentSubCategory;

    @OneToMany(mappedBy = "content")
    private List<ContentFavorite> contentFavorites = new ArrayList<>();
}
