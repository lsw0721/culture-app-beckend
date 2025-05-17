package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.domain.Comment;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.CommentRequestDto;
import cultureinfo.culture_app.dto.response.CommentDto;
import cultureinfo.culture_app.repository.CommentRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import cultureinfo.culture_app.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final SecurityUtil securityUtil;
    private final MemberRepository memberRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    // 댓글 등록
    @PostMapping("/{articleId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long articleId,
            @RequestBody CommentRequestDto requestDto) {
        
        Long memberId = securityUtil.getCurrentId();
        CommentDto created = commentService.createComment(articleId, memberId, requestDto);
        return ResponseEntity.ok(created);
    }

    // 게시글 댓글 전체 조회
    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long articleId) {
        return ResponseEntity.ok(commentService.getCommentsByArticleId(articleId));
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto) {
        
        //현재 로그인한 사용자의 id를 가져오는 함수
        Long memberId = securityUtil.getCurrentId();

        //가져온 id 정보로 user 정보 가져오기
        Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        String role = member.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        //admin이거나 본인일 때 수정가능하게 설정
        if(!(comment.getMember().getId() == memberId) && role =="ROLE_USER"){
                throw new AccessDeniedException("본인의 댓글만 수정할 수 있습니다.");
        }

        CommentDto updated = commentService.updateComment(commentId, requestDto);
        return ResponseEntity.ok(updated);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {

        //현재 로그인한 사용자의 id를 가져오는 함수
        Long memberId = securityUtil.getCurrentId();

        //가져온 id 정보로 user 정보 가져오기
        Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        String role = member.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        //admin이거나 본인일 때 수정가능하게 설정
        if(!(comment.getMember().getId() == memberId) && role =="ROLE_USER"){
                throw new AccessDeniedException("본인의 댓글만 삭제할 수 있습니다.");
        }

        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
