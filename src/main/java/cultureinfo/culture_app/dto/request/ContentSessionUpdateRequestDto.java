package cultureinfo.culture_app.dto.request;

import lombok.*;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDate;

//일별 세션 수정 요청
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentSessionUpdateRequestDto {
    @NotNull
    private LocalDate sessionDate;
    private String infoJson;
}
