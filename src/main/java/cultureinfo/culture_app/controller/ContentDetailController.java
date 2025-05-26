package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.dto.request.ContentDetailCreateRequestDto;
import cultureinfo.culture_app.dto.request.ContentDetailUpdateRequestDto;
import cultureinfo.culture_app.dto.request.ContentSearchRequestDto;
import cultureinfo.culture_app.dto.response.ContentDetailDto;
import cultureinfo.culture_app.dto.response.ContentSummaryDto;
import cultureinfo.culture_app.service.ContentDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.IIOException;
import java.io.IOException;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentDetailController {

    private final ContentDetailService contentDetailService;

    // 목록 조회: 대분류(main), 중분류(sub), 소분류(small) 모두 옵션으로 받아 필터링
    // GET /api/contents?mainCategoryId=&subcategoryId=&smallCategoryId=&keyword=&...

    @GetMapping
    public Slice<ContentSummaryDto> search(@Valid ContentSearchRequestDto req) {
        return contentDetailService.search(req);
    }


     // 단일 콘텐츠 상세 조회 (로그인 필요)
     // GET /api/contents/{id}
    @GetMapping("/{id}")
    public ContentDetailDto getDetail(@PathVariable Long id) {
        return contentDetailService.getContentDetail(id);
    }


     // 콘텐츠 생성
     // POST /api/contents
    @PostMapping
    public ContentDetailDto create (
            @RequestBody @Valid ContentDetailCreateRequestDto req
    ) {
        return contentDetailService.createContentDetail(req);
    }


     // 콘텐츠 수정
     // PUT /api/contents/{id}
    @PutMapping("/{id}")
    public ContentDetailDto update(
            @PathVariable Long id,
            @RequestBody @Valid ContentDetailUpdateRequestDto req
    ) {
        return contentDetailService.updateContentDetail(id, req);
    }


     // 콘텐츠 삭제
     // DELETE /api/contents/{id}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contentDetailService.deleteContentDetail(id);
        return ResponseEntity.noContent().build();
    }

}
