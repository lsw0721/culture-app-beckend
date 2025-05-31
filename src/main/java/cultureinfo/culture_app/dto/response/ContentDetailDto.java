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
    private String picture; // 들어가는 사진 url
    private String price; // 콘텐츠 가격
    /*
    private String artistName; // 가수 이름
    private String sportTeamName; // 스포츠 팀 이름
    private String brandName; // 팝업 브랜드 이름
    */
    private String subjectName;
    private String subject;
    private String link;
    private List<ContentSessionDto> sessions; // 일별 세션
    //private String detailsJson; // 세부정보


    public static ContentDetailDto from(ContentDetail d, boolean fav) {
        List<ContentSessionDto> sessions = d.getSessions().stream()
                .map(s -> new ContentSessionDto(s.getId(), s.getSessionDate(), s.getInfoJson(), s.getPicture()))
                .toList();

        return ContentDetailDto.builder()
                .id(d.getId())
                .contentName(d.getContentName())
                .picture(d.getPicture())
                .startDateTime(d.getStartDateTime())
                .endDateTime(d.getEndDateTime())
                .location(d.getLocation())
                .address(d.getAddress())
                .price(d.getPrice())
                .isFavorite(fav)
                .favoriteCount(d.getFavoriteCount())
                .sessions(sessions)
                .subjectName(d.getSubjectName())
                .subject(d.getSubject())
                .link(d.getLink())
                /*
                .artistName(d.getArtistName())
                .sportTeamName(d.getSportTeamName())
                .brandName(d.getBrandName())
                .detailsJson(d.getDetailsJson())

                 */
                .build();
    }

    public static List<ContentDetailDto> fromList(List<ContentDetail> details, boolean fav) {
        return details.stream()
            .map(d -> from(d, fav))  // 기존의 from 메서드를 재사용
            .collect(Collectors.toList());
    }
}
