package cultureinfo.culture_app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//콘텐츠 수정 요청용 dto
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentDetailUpdateRequestDto {
    private Long id; // 콘텐츠 상세 id
    private String contentName; // 콘텐츠 이름
    private LocalDateTime startDateTime; // 콘텐츠 시작 시간
    private LocalDateTime endDateTime; // 콘텐츠 종료 시간
    private Long favoriteCount; // 찜 개수
    private String picture; // 들어가는 사진 url
    private String location; // 콘텐츠 위치
    private Long price; // 콘텐츠 가격
    private String artistName; // 가수 이름
    private String sportTeamName; // 스포츠 팀 이름
    private String brandName; // 팝업 브랜드 이름
    private String detailsJson;
}
