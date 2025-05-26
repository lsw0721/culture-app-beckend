package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.dto.request.ArticleRequestDto;
import cultureinfo.culture_app.dto.request.ArticleUpdateDto;
import cultureinfo.culture_app.dto.response.ArticleDto;
import cultureinfo.culture_app.dto.response.ArticleSummaryDto;
import cultureinfo.culture_app.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    //게시글 생성
    @PostMapping
    public ResponseEntity<ArticleDto> createArticle(@RequestBody ArticleRequestDto requestDto) {
        ArticleDto created = articleService.createArticle(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticle(@PathVariable Long id) {
        ArticleDto article = articleService.getArticle(id);
        return ResponseEntity.ok(article);
    }

    // 게시글 전체 조회 -> slice 적용
    @GetMapping
    public ResponseEntity<Slice<ArticleSummaryDto>> getAll(
            @PageableDefault(size = 20, sort = "createDate", direction = Sort.Direction.DESC)
            Pageable pageable){
        Slice<ArticleSummaryDto> slice = articleService.getAllArticles(pageable);
        return ResponseEntity.ok(slice);
    }


    //게시글 검색 -> slice 적용
    @GetMapping("/search")
    public ResponseEntity<Slice<ArticleSummaryDto>> searchArticles(
            @RequestParam String keyword,
            @PageableDefault(size = 20, sort = "createDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Slice<ArticleSummaryDto> slice = articleService.searchArticles(keyword, pageable);
        return ResponseEntity.ok(slice);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDto> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleUpdateDto requestDto) {
        ArticleDto updated = articleService.updateArticle(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
