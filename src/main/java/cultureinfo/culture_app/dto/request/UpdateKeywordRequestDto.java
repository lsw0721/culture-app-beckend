package cultureinfo.culture_app.dto.request;

import cultureinfo.culture_app.domain.type.Keyword1;
import cultureinfo.culture_app.domain.type.Keyword2;
import cultureinfo.culture_app.domain.type.Keyword3;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateKeywordRequestDto {
    private Keyword1 keyword1;
    private Keyword2 keyword2;
    private Keyword3 keyword3;
}
