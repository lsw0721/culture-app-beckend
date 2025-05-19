package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ContentSmallCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ContentSmallCategoryName; // 콘텐츠 소분류 "2025 봄 대동제" 등

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_subcategory_id")
    private ContentSubcategory contentSubcategory;

    @OneToMany(mappedBy = "contentSmallCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentDetail> details = new ArrayList<>();

}
