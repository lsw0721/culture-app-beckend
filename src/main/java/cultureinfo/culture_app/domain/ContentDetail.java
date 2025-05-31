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

    @Column(nullable = false)
    private String address; // 콘텐츠 지역(필수)/ 도로명주소

    @Column(nullable = true)
    private String price; // 콘텐츠 가격(옵션)

    @Column(nullable = true)
    private String picture; // 콘텐츠 사진 - URL 경로 설계 등 필요(옵션)

    @Column(nullable = true)
    private String subject; // 주제, 줄거리(옵션)

    @Column(nullable = true)
    private String link; // 링크(옵션)

    @Column(nullable = false)
    private Long favoriteCount = 0L; // 찜 개수(필수)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "content_sub_category_id")
    private ContentSubCategory contentSubcategory;

    //콘텐츠 찜
    @OneToMany(mappedBy = "contentDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentFavorite> contentFavorite = new ArrayList<>();

    //일별 콘텐츠 상세
    @OneToMany(mappedBy = "contentDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentSession> sessions = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "content_detail_subjects",
            joinColumns = @JoinColumn(name = "content_detail_id")
    )
    @Column(nullable = false)
    private List<String> subjectNames = new ArrayList<>();

    @Builder
    public ContentDetail(String contentName,
                         LocalDateTime startDateTime,
                         LocalDateTime endDateTime,
                         String location,
                         String address,
                         String price,
                         String picture,
                         List<String> subjectNames,
                         String subject,
                         String link,
                         ContentSubCategory contentSubcategory) {
        this.contentName = contentName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.location = location;
        this.address = address;
        this.price = price;
        this.picture = picture;
        this.subjectNames = subjectNames != null ? subjectNames : new ArrayList<>();
        this.subject = subject;
        this.link = link;
        this.contentSubcategory = contentSubcategory;
        this.favoriteCount = 0L;
    }

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

    // ↓↓↓ 이 부분도 List<String>을 그대로 덮어쓰도록 수정했습니다 ↓↓↓
    public void changeSubjectNames(List<String> subjectNames) {
        this.subjectNames = subjectNames != null ? subjectNames : new ArrayList<>();
    }

    // 설명(주제) 변경
    public void changeSubject(String subject) {
        this.subject = subject;
    }

    // 링크 변경
    public void changeLink(String link) {
        this.link = link;
    }

}
