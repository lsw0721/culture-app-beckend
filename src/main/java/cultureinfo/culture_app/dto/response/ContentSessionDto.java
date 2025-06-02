package cultureinfo.culture_app.dto.response;

import cultureinfo.culture_app.domain.ContentSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ContentSessionDto {
    private Long contentDetailId;
    private String sessionDate;
    private List<String> booths;
    private List<String> artistNames;

    public static ContentSessionDto from(ContentSession session) {
        return new ContentSessionDto(
                session.getId(),
                session.getSessionDate(),
                session.getBooths(),
                session.getArtistNames()
        );
    }
}
