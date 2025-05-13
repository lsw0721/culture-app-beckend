package cultureinfo.culture_app.dto.request;

import cultureinfo.culture_app.domain.type.ArticleCategory;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ArticleRequestDto {
    private String title;
    private String body;
    private ArticleCategory category;
    private Long contentId;
}
