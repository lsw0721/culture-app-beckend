package cultureinfo.culture_app.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announcement {
    @Id @GeneratedValue
    private Long id;

    private String title; // 공지사항 제목
    private String body; // 공지사항 내용

    private LocalDateTime createDate;

    @Builder
    public Announcement(String title, String body, LocalDateTime createDate) {
        this.title = title;
        this.body = body;
        this.createDate = createDate;
    }

    //공지사항 수정
    public void update(String title, String body) {
        this.title = title;
        this.body = body;
    }
    
}
