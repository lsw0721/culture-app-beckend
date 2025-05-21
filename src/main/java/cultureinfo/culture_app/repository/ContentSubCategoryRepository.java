package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.ContentSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentSubCategoryRepository extends JpaRepository<ContentSubcategory, Long> {
}
