package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
//콘텐츠 세부
public class ContentDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contentName; // 콘텐츠 이름

    private LocalDateTime startDateTime; // 시작일자
    private LocalDateTime endDateTime; // 종료일자

    private String location; // 콘텐츠 지역
    private Long price = 0L; // 콘텐츠 가격

    private String picture; // 콘텐츠 사진 - URL 경로 설계 등 필요

    //가수명, 스포츠 팀명,

    @Lob
    private String details; // 각 컨테츠 별로 바뀌거나 조회가 필요 없는 세부내용

    private Long favoriteCount; // 찜 개수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_small_category_id")
    private ContentSmallCategory contentSmallCategory;

    @OneToMany(mappedBy = "contentDetail")
    private List<ContentFavorite> contentFavorite = new ArrayList<>();

    public void increaseFavoriteCount() { // 찜 개수 증가
        this.favoriteCount++;
    }

    public void decreaseFavoriteCount() { // 찜 개수 감소
        if (this.favoriteCount > 0) {
            this.favoriteCount--;
        }

    }


}
