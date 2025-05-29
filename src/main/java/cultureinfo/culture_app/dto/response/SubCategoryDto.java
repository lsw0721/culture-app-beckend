package cultureinfo.culture_app.dto.response;
import cultureinfo.culture_app.domain.ContentSubCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SubCategoryDto {

    //중분류
    private Long id;

    //중분류 이름
    private String name;

    public static SubCategoryDto from(ContentSubCategory sub) {
        return SubCategoryDto.builder()
                .id(sub.getId())
                .name(sub.getName())
                .build();
    }
}
