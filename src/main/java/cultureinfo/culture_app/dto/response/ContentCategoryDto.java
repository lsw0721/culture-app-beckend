package cultureinfo.culture_app.dto.response;

import cultureinfo.culture_app.domain.ContentCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ContentCategoryDto {
    //대분류 id
    private Long id;

    //대분류 이름
    private String name;

    public static ContentCategoryDto from(ContentCategory contentCategory) {
        return ContentCategoryDto.builder()
                .id(contentCategory.getId())
                .name(contentCategory.getName())
                .build();
    }
}
