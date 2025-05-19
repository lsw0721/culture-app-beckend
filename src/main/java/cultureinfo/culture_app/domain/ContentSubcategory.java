package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
//콘텐츠 중분류
public class ContentSubcategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contentSubcategoryName; // 콘텐츠 중분류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_category_id")
    private ContentCategory contentCategory;

    @OneToMany(mappedBy = "contentSubcategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentSmallCategory> smallCategories = new ArrayList<>();
}
