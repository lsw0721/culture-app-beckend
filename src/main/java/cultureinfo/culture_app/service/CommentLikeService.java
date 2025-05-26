package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Comment;
import cultureinfo.culture_app.domain.CommentLike;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.response.CommentLikeDto;
import cultureinfo.culture_app.exception.CustomException;
import cultureinfo.culture_app.exception.ErrorCode;
import cultureinfo.culture_app.repository.CommentLikeRepository;
import cultureinfo.culture_app.repository.CommentRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public CommentLikeDto toggleLike(Long commentId) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean liked;

        if (commentLikeRepository.existsByCommentIdAndMemberId(commentId, memberId)) {
            CommentLike existing = commentLikeRepository
                    .findByCommentIdAndMemberId(commentId, memberId)
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_LIKE_NOT_FOUND));
            commentLikeRepository.delete(existing);
            comment.decreaseLikeCount();
            liked = false;
        } else {
            CommentLike like = CommentLike.builder()
                    .comment(comment)
                    .member(member)
                    .build();
            commentLikeRepository.save(like);
            comment.increaseLikeCount();
            liked = true;
        }

        return new CommentLikeDto(liked, comment.getLikeCount());
    }
}
