package cultureinfo.culture_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContentFavoriteDto {
    private boolean isFavorite;
    private Long favoriteCount;
}
