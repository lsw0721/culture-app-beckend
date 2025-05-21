package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.ContentSmallCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentSmallCategoryRepository extends JpaRepository<ContentSmallCategory, Long> {
}
