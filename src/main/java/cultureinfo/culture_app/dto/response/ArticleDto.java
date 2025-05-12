package cultureinfo.culture_app.dto.response;

import cultureinfo.culture_app.domain.type.ArticleCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDto {
    private Long id;
    private String title;
    private String body;
    private ArticleCategory category;
    private LocalDateTime createDateTime;
    private Long likeCount;
    private Long commentCount;
}
