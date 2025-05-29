package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentListRequestDto {
    /** 조회할 중분류 ID */
    private Long subCategoryId;
    private String sortBy;

    @Min(0)
    @Builder.Default
    private int page = 0;

    @Min(1)
    @Builder.Default
    private int size = 20;
}

