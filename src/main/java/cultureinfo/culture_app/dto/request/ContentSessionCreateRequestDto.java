package cultureinfo.culture_app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor

//일별 세션 생성 요청
public class ContentSessionCreateRequestDto {
    @NotNull
    private Long contentDetailId;

    @NotNull
    private LocalDate sessionDate;

    private String infoJson;

    private MultipartFile pictureFile;
}
