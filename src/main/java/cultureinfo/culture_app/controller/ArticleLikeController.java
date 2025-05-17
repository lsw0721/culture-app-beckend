package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.dto.response.ArticleLikeDto;
import cultureinfo.culture_app.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cultureinfo.culture_app.security.SecurityUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;
    private final SecurityUtil securityUtil;

    // 좋아요 토글
    @PostMapping("/{articleId}/like")
    public ResponseEntity<ArticleLikeDto> toggleLike(
            @PathVariable Long articleId) {
        
        Long memberId = securityUtil.getCurrentId();
        ArticleLikeDto response = articleLikeService.toggleLike(articleId, memberId);
        return ResponseEntity.ok(response);
    }
}
