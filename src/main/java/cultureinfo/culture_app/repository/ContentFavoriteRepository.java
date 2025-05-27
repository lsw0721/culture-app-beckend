package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.ContentFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentFavoriteRepository extends JpaRepository<ContentFavorite, Long> {
    List<ContentFavorite> findAllByMemberIdAndContentDetailIdIn(Long memberId, List<Long> contentDetailIds);

    Optional<ContentFavorite> findByMemberIdAndContentDetailId(Long memberId, Long contentDetailId);
    boolean existsByMemberIdAndContentDetailId(Long memberId, Long contentDetailId);
    List<ContentFavorite> findAllByMemberId(Long memberId);
}
