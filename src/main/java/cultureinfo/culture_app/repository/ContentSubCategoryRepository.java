package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.ContentSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentSubCategoryRepository extends JpaRepository<ContentSubCategory, Long> {
    List<ContentSubCategory> findAllByContentCategoryId(Long contentCategoryId);
}
