package cultureinfo.culture_app.domain;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//일별 세션(소분류 상세) - 축제 전용
public class ContentSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ContentDetail contentDetail; //

    @Column(nullable = false)
    private String sessionDate; // 1일차, 2일차, 3일차

    @ElementCollection
    @CollectionTable(name = "session_booths", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "booth_name")
    private List<String> booths = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "session_artists", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "artist_name")
    private List<String> artistNames = new ArrayList<>();

    public void changeSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public void changeArtistNames(List<String> artistNames) {
        this.artistNames = artistNames;
    }

    public void changeBooths(List<String> booths) {
        this.booths = booths;
    }

}
