package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.dto.response.CommentLikeDto;
import cultureinfo.culture_app.security.SecurityUtil;
import cultureinfo.culture_app.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;
    private final SecurityUtil securityUtil;

    //좋아요 토글
    @PostMapping("/{commentId}/like")
    public ResponseEntity<CommentLikeDto> toggleLike(
            @PathVariable Long commentId) {
        
        Long memberId = securityUtil.getCurrentId();
        CommentLikeDto response = commentLikeService.toggleLike(commentId, memberId);
        return ResponseEntity.ok(response);
    }
}
