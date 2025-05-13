package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.Comment;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.CommentRequestDto;
import cultureinfo.culture_app.dto.response.CommentDto;
import cultureinfo.culture_app.repository.ArticleRepository;
import cultureinfo.culture_app.repository.CommentRepository;
import cultureinfo.culture_app.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    // 댓글 등록
    @Transactional
    public CommentDto createComment(Long articleId, Long memberId, CommentRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        Comment comment = new Comment(requestDto.getCommentContent(), article, member);
        Comment saved = commentRepository.save(comment);
        article.increaseCommentCount();
        return CommentDto.from(saved);
    }

    // 게시글의 모든 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        return comments.stream()
                .map(CommentDto::from)
                .collect(Collectors.toList());
    }

    //삭제 및 수정은 작성자만 가능하게 권한 부여 필요

    // 댓글 수정
    @Transactional
    public CommentDto updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));
        comment.updateContent(requestDto.getCommentContent());
        return CommentDto.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
        comment.getArticle().decreaseCommentCount();
    }
}
