package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ContentFavorite {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;
}
