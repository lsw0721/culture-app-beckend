package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.type.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    Optional<Article> findById(Long id);
    List<Article> findAllByMember_id(Long member_id);
    List<Article> findAllByCategory(ArticleCategory category);
}
