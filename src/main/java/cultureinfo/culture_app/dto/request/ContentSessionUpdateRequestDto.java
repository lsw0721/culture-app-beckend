package cultureinfo.culture_app.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

//일별 세션 수정 요청
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentSessionUpdateRequestDto {
    @NotNull
    private String sessionDate;

    private List<String> booths;

    private List<String> artistNames;
}
