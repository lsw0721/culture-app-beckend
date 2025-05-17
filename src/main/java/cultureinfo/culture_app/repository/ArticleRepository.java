package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {
    Optional<Article> findById(Long id);
}
