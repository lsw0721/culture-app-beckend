package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentDetailCreateRequestDto {
    @NotNull
    private Long contentSubcategoryId;

    @NotBlank
    private String contentName;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @NotBlank
    private String location;

    private String address;

    private String price;

    //업로드된 파일을 받을 필드
    private MultipartFile thumbnailFile;
    private List<MultipartFile> detailFiles;
    private List<String> subjectNames;
    private String subject;
    private String link;



}
