package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.ContentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentCategoryRepository extends JpaRepository<ContentCategory, Long> {
}
