package cultureinfo.culture_app.controller;


import cultureinfo.culture_app.dto.request.CommentRequestDto;
import cultureinfo.culture_app.dto.response.CommentDto;
import cultureinfo.culture_app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/{articleId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long articleId,
            @RequestBody CommentRequestDto requestDto) {

        CommentDto created = commentService.createComment(articleId, requestDto);
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

        CommentDto updated = commentService.updateComment(commentId, requestDto);
        return ResponseEntity.ok(updated);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
