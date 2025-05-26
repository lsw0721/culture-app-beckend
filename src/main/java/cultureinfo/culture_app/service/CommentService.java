package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.Comment;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.CommentRequestDto;
import cultureinfo.culture_app.dto.response.CommentDto;
import cultureinfo.culture_app.exception.CustomException;
import cultureinfo.culture_app.exception.ErrorCode;
import cultureinfo.culture_app.repository.ArticleRepository;
import cultureinfo.culture_app.repository.CommentRepository;
import cultureinfo.culture_app.repository.MemberRepository;

import cultureinfo.culture_app.security.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final SecurityUtil securityUtil;

    // 댓글 등록: 로그인한 사용자만 가능
    @Transactional
    public CommentDto createComment(Long articleId, CommentRequestDto requestDto) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        Comment comment = Comment.builder()
                .commentContent(requestDto.getCommentContent())
                .member(member)
                .article(article)
                .createBy(member.getUsername())
                .createDate(LocalDateTime.now())
                .build();
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

    // 댓글 수정: 
    @Transactional
    public CommentDto updateComment(Long commentId, CommentRequestDto requestDto) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean isOwner = comment.getMember().getId().equals(memberId);
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isOwner && !isAdmin) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MODIFICATION);
        }
        
        comment.updateContent(requestDto.getCommentContent());
        comment.setLastModifiedBy(comment.getMember().getUsername());
        comment.setLastModifiedDate(LocalDateTime.now());
        return CommentDto.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean isOwner = comment.getMember().getId().equals(memberId);
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isOwner && !isAdmin) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DELETION);
        }
        commentRepository.delete(comment);
        comment.getArticle().decreaseCommentCount();
    }
}
