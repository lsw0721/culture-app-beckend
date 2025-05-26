package cultureinfo.culture_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ContentSessionDto {
    private Long id;
    private LocalDate sessionDate;
    private String infoJson;
    private String picture;
}
