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
//콘텐츠 대분류
public class ContentCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 대분류 이름

    @OneToMany(mappedBy = "contentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentSubcategory> subcategories = new ArrayList<>();
}

