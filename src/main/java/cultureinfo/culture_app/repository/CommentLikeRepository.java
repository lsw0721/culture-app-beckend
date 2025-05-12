package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentIdAndMemberId(Long commentId, Long memberId);
    boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);
}
