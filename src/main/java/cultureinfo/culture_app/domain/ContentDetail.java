package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
//콘텐츠 세부 (소분류 상세의 루트)
public class ContentDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contentName; // 콘텐츠 이름(필수)

    @Column(nullable = false)
    private LocalDateTime startDateTime;// 시작일자(필수)

    @Column(nullable = false)
    private LocalDateTime endDateTime; // 종료일자(필수)

    @Column(nullable = false)
    private String location; // 콘텐츠 지역(필수)

    @Column(nullable = true)
    private Long price = 0L; // 콘텐츠 가격(옵션)

    @Column(nullable = true)
    private String picture; // 콘텐츠 사진 - URL 경로 설계 등 필요(옵션)

    @Column(nullable = true)
    private String artistName; // 가수(그룹)명(옵션)

    @Column(nullable = true)
    private String sportTeamName;

    @Column(nullable = true)// 스포츠 팀명(옵션)
    private String brandName; // 브랜드 명(옵션)

    @Lob
    @Column(nullable = true)
    private String detailsJson; // 각 컨테츠 별로 바뀌거나 조회가 필요 없는 세부내용 Json(옵션)

    @Column(nullable = false)
    private Long favoriteCount = 0L; // 찜 개수(필수)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "content_small_category_id")
    private ContentSmallCategory contentSmallCategory;

    //콘텐츠 찜
    @OneToMany(mappedBy = "contentDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentFavorite> contentFavorite = new ArrayList<>();

    //일별 콘텐츠 상세
    @OneToMany(mappedBy = "contentDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentSession> sessions = new ArrayList<>();

    //찜 개수 증가
    public void increaseFavoriteCount() { // 찜 개수 증가
        this.favoriteCount++;
    }

    // 찜 개수 감소
    public void decreaseFavoriteCount() {
        if (this.favoriteCount > 0) {
            this.favoriteCount--;
        }

    }

    //콘텐츠 이름 변경
    public void changeContentName(String name) {
        if(name == null || name.isBlank())
            throw new IllegalArgumentException("콘텐츠 이름은 비울 수 없습니다.");
        this.contentName = name;
    }

    // 시작/종료일자 동시 변경
    public void changePeriod(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new IllegalArgumentException("종료일자는 시작일자 이후여야 합니다.");
        }
        Optional.ofNullable(start).ifPresent(s -> this.startDateTime = s);
        Optional.ofNullable(end).ifPresent(e -> this.endDateTime = e);
    }

    // 위치 변경 (옵션)
    public void changeLocation(String location) {
        Optional.ofNullable(location)
                .filter(loc -> !loc.isBlank())
                .ifPresent(loc -> this.location = loc);
    }

    // 사진 URL 변경 (옵션)
    public void changePicture(String picture) {
        Optional.ofNullable(picture)
                .filter(url -> !url.isBlank())
                .ifPresent(url -> this.picture = url);
    }

    //아티스트 명 변경
    public void changeArtistName(String artist) {
        Optional.ofNullable(artist)
                .filter(a -> !a.isBlank())
                .ifPresent(a -> this.artistName = a);
    }

    //스포츠 팀 명 변경
    public void changeSportTeamName(String team) {
        Optional.ofNullable(team)
                .filter(t -> !t.isBlank())
                .ifPresent(t -> this.sportTeamName = t);
    }

    //브랜드 명 변경
    public void changeBrandName(String brand) {
        Optional.ofNullable(brand)
                .filter(b -> !b.isBlank())
                .ifPresent(b -> this.brandName = b);
    }

    // 상세 JSON 변경 (옵션)
    public void changeDetailsJson(String json) {
        Optional.ofNullable(json)
                .ifPresent(j -> this.detailsJson = j);
    }
    @Builder
    public ContentDetail(String contentName,
                         LocalDateTime startDateTime,
                         LocalDateTime endDateTime,
                         String location,
                         Long price,
                         String picture,
                         String artistName,
                         String sportTeamName,
                         String brandName,
                         String detailsJson,
                         ContentSmallCategory contentSmallCategory) {
        this.contentName = contentName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.price = price == null ? 0L : price;
        this.picture = picture;
        this.artistName = artistName;
        this.sportTeamName = sportTeamName;
        this.brandName = brandName;
        this.detailsJson = detailsJson;
        this.contentSmallCategory = contentSmallCategory;
        this.favoriteCount = 0L;
    }

}
