package cultureinfo.culture_app.dto.response;

import cultureinfo.culture_app.domain.ContentDetail;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ContentSummaryDto {
    private Long id;
    private String contentName; // 콘텐츠 제목
    private List<String> imageUrls; // 콘텐츠 목록용 사진 url
    private LocalDateTime startDateTime; // 시작 날짜
    private LocalDateTime endDateTime; // 종료 날짜

    public static ContentSummaryDto of(ContentDetail d) {
        return ContentSummaryDto.builder()
                .id(d.getId())
                .contentName(d.getContentName())
                .imageUrls(d.getImageUrls())
                .startDateTime(d.getStartDateTime())
                .endDateTime(d.getEndDateTime())
                .build();
    }

}
