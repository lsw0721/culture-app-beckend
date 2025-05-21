package cultureinfo.culture_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentDetailCreateRequestDto {
    @NotNull
    private Long contentSmallCategoryId;

    @NotBlank
    private String contentName;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @NotBlank
    private String location;

    private Long price;
    private String picture;
    private String artistName;
    private String sportTeamName;
    private String brandName;
    private String detailsJson;
}
