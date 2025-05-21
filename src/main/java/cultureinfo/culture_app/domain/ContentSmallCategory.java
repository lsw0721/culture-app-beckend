package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
//콘텐츠 소분류
public class ContentSmallCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 콘텐츠 소분류 "2025 봄 대동제" 등

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // optional = false 조건은 연관관계가 무조건 있어야 함을 표시
    @JoinColumn(name = "content_subcategory_id")
    private ContentSubcategory contentSubcategory;

    @OneToMany(mappedBy = "contentSmallCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentDetail> details = new ArrayList<>();

}
