package cultureinfo.culture_app.dto.response;

import cultureinfo.culture_app.domain.ContentDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Builder
@AllArgsConstructor
public class ContentDetailDto {
    private Long id;
    private String contentName;
    private String picture;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean isFavorite;
    private Long favoriteCount;

    public static ContentDetailDto from(ContentDetail detail, boolean isFavorited) {
        return ContentDetailDto.builder()
                .id(detail.getId())
                .contentName(detail.getContentName())
                .picture(detail.getPicture())
                .startDateTime(detail.getStartDateTime())
                .endDateTime(detail.getEndDateTime())
                .isFavorite(isFavorited)
                .favoriteCount(detail.getFavoriteCount())
                .build();
    }
}
