package cultureinfo.culture_app.dto.request;


import cultureinfo.culture_app.domain.type.ArticleCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

//게시글 수정 dto
public class ArticleUpdateDto {
    private String title;
    private String body;
    private ArticleCategory category;
}
