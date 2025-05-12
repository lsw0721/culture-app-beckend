package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long articleId); // 특정 게시글의 댓글 리스트
}
