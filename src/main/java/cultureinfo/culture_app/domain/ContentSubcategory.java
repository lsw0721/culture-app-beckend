package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ContentSubcategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String contentSubcategoryName; // 콘텐츠 중분류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentCategory_id")
    private ContentCategory contentCategory;
}
