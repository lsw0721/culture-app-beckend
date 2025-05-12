package cultureinfo.culture_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleLikeDto {
    private Boolean liked;
    private Long likeCount;
}
