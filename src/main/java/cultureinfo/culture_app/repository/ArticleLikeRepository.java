package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByArticleIdAndMemberId(Long articleId, Long memberId);
    boolean existsByArticleIdAndMemberId(Long articleId, Long memberId);
}
