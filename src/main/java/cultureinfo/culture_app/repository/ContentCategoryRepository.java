package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.ContentCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentCategoryRepository extends JpaRepository<ContentCategory, Long> {

    // 중분류까지 함께 로딩하도록 EntityGraph 적용
    @EntityGraph(attributePaths = {"subCategories"})
    List<ContentCategory> findAll();
}
