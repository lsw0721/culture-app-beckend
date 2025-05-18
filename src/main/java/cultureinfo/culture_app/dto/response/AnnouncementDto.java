package cultureinfo.culture_app.dto.response;

import java.time.LocalDateTime;

import cultureinfo.culture_app.domain.Announcement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementDto {
    private Long id;
    private String title;
    private String body;
    private LocalDateTime createDate;

    public static AnnouncementDto from(Announcement announcement) {
        return AnnouncementDto.builder()
                .id(announcement.getId())
                .title(announcement.getTitle())
                .body(announcement.getBody())
                .createDate(announcement.getCreateDate())
                .build();
    }
}
