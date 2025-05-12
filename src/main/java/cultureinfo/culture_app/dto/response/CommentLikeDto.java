package cultureinfo.culture_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentLikeDto {
    private boolean liked;
    private Long likeCount;
}
