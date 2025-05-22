package cultureinfo.culture_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//콘텐츠 찜 토글/상태 dto
public class ContentFavoriteDto {
    private boolean isFavorite;
    private Long favoriteCount;
}
