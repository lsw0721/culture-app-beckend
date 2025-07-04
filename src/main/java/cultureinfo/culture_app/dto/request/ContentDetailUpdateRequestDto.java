package cultureinfo.culture_app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

//콘텐츠 수정 요청용 dto
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContentDetailUpdateRequestDto {
    private Long id; // 콘텐츠 상세 id
    private String contentName; // 콘텐츠 이름
    private LocalDateTime startDateTime; // 콘텐츠 시작 시간
    private LocalDateTime endDateTime; // 콘텐츠 종료 시간
    private Long favoriteCount; // 찜 개수
    private MultipartFile thumbnailFile;
    private List<MultipartFile> detailFiles;
    private String location; // 콘텐츠 위치
    private String address;
    private String price; // 콘텐츠 가격
    private List<String> subjectNames;
    private String subject;
    private String link;
}
