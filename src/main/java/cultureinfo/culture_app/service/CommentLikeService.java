package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Comment;
import cultureinfo.culture_app.domain.CommentLike;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.response.CommentLikeDto;
import cultureinfo.culture_app.repository.CommentLikeRepository;
import cultureinfo.culture_app.repository.CommentRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentLikeDto toggleLike(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        boolean liked;

        if (commentLikeRepository.existsByCommentIdAndMemberId(commentId, memberId)) {
            CommentLike existing = commentLikeRepository
                    .findByCommentIdAndMemberId(commentId, memberId)
                    .orElseThrow(() -> new EntityNotFoundException("댓글 좋아요가 존재하지 않습니다."));
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
