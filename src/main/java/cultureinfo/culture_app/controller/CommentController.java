package cultureinfo.culture_app.controller;


import cultureinfo.culture_app.dto.request.CommentRequestDto;
import cultureinfo.culture_app.dto.response.CommentDto;
import cultureinfo.culture_app.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
        try{
            CommentDto created = commentService.createComment(articleId, requestDto);
            return ResponseEntity.ok(created);
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(401).build(); //멤버가 존재하지 않거나 콘텐츠가 존재하지 않을때
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
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
        try{
            CommentDto updated = commentService.updateComment(commentId, requestDto);
            return ResponseEntity.ok(updated);
        } catch(AccessDeniedException e){
            return ResponseEntity.status(401).build();
        } catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        try{
            commentService.deleteComment(commentId);
            return ResponseEntity.noContent().build();
        } catch(AccessDeniedException e){
            return ResponseEntity.status(401).build();
        } catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }
}
