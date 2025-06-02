package cultureinfo.culture_app.dto.response;

import cultureinfo.culture_app.domain.ContentDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor

//콘텐츠 상세페이지 용 responseDto
public class ContentDetailDto {
    private Long id; // 콘텐츠 상세 id
    private String contentName; // 콘텐츠 이름
    private LocalDateTime startDateTime; // 콘텐츠 시작 시간
    private LocalDateTime endDateTime; // 콘텐츠 종료 시간
    private String location; // 콘텐츠 위치
    private String address;
    private boolean isFavorite; // 찜 여부
    private Long favoriteCount; // 찜 개수
    private List<String> imageUrls; // 들어가는 사진 url
    private String price; // 콘텐츠 가격
    private List<String> subjectNames;
    private String subject;
    private String link;


    public static ContentDetailDto from(ContentDetail d, boolean fav) {
        return ContentDetailDto.builder()
                .id(d.getId())
                .contentName(d.getContentName())
                //.picture(d.getPicture())
                .imageUrls(d.getImageUrls())
                .startDateTime(d.getStartDateTime())
                .endDateTime(d.getEndDateTime())
                .location(d.getLocation())
                .address(d.getAddress())
                .price(d.getPrice())
                .isFavorite(fav)
                .favoriteCount(d.getFavoriteCount())
                .subjectNames(d.getSubjectNames())
                .subject(d.getSubject())
                .link(d.getLink())
                .build();
    }

    public static List<ContentDetailDto> fromList(List<ContentDetail> details, boolean fav) {
        return details.stream()
            .map(d -> from(d, fav))  // 기존의 from 메서드를 재사용
            .collect(Collectors.toList());
    }
}
