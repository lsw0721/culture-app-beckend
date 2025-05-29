package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//콘텐츠 중분류
public class ContentSubCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 콘텐츠 중분류

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // optional = false 조건은 연관관계가 무조건 있어야 함을 표시
    @JoinColumn(name = "content_category_id")
    private ContentCategory contentCategory;

    @OneToMany(mappedBy = "contentSubcategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentDetail> details = new ArrayList<>();
}
