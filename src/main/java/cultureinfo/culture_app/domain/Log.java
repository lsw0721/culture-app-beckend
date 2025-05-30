package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter

//삭제 필요..?
public class Log {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private ContentDetail content;
}
