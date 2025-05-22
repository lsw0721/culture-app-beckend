package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//일별 세션(소분류 상세)
public class ContentSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ContentDetail contentDetail; //

    @Column(nullable = false)
    private LocalDate sessionDate;

    @Lob
    @Column(nullable = true)
    private String infoJson; // 세션별 추가 정보

    public void changeSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public void changeInfoJson(String infoJson) {
        this.infoJson = infoJson;
    }
}
