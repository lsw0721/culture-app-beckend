package cultureinfo.culture_app.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
//콘텐츠 대분류
public class ContentCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String contentCategoryName; // 대분류 이름

    @OneToMany(mappedBy = "contentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentSubcategory> subcategories = new ArrayList<>();
}
