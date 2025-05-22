package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//콘텐츠 검색 요청용 dto
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
//보류
public class ContentSearchRequestDto {
    private Long categoryId;    // 대분류(또는 중분류·소분류) ID
    private Long subCategoryId; // 중분류
    private Long smallCategoryId; // 소분류
    private String keyword;     // 콘텐츠 이름 검색
    private String artistName; // 가수명 검색 키워드
    private String sportTeamName;   // 스포츠 팀명 검색 키워드
    private String brandName;       // 브랜드명 검색 키워드
    private String sortBy;      // "favoriteCount", "startDateTime" 등

    @Min(0)
    private int   page = 0;         // 0부터 시작
    @Min(1)
    private int   size = 20;         // 페이지 크기
}
