package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;

//콘텐츠 검색 요청용 dto
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor


public class ContentSearchRequestDto {
    //이 정보만 필요
    private Long subCategoryId; // 중분류

    private String keyword;     // 콘텐츠 이름 검색
    private String subjectName; // 가수명, 스포츠 팀명 등등
    private String sortBy;      // "favoriteCount", "startDateTime" 등

    @Min(0)
    private int   page = 0;// 0부터 시작

    @Min(1)
    private int   size = 20;         // 페이지 크기
}
