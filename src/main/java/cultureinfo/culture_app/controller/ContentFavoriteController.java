package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.dto.response.ContentFavoriteDto;
import cultureinfo.culture_app.service.ContentFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class ContentFavoriteController {
    private final ContentFavoriteService contentFavoriteService;

    // 콘텐츠 찜 토글 (추천 로그용으로도 활용 가능)
    @PostMapping("/{contentDetailId}")
    public ContentFavoriteDto toggleFavorite(@PathVariable Long contentDetailId) {
        return contentFavoriteService.toggleFavorite(contentDetailId);
    }

    //로그를 얻기 위한 메소드들 추가 필요.
    // 내가 찜한 콘텐츠 목록
    //찜 통계 조회
    //등등..
}
