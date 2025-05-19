package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.dto.response.ContentDetailDto;
import cultureinfo.culture_app.service.ContentDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentDetailController {

    private final ContentDetailService contentDetailService;

    //콘텐츠 리스트 조회(카테고리 필터, 키워드 검색, 정렬, 페이징, 찜 여부 포함)
    //url에 페이징 정보가 알아서 들어감
    @GetMapping
    public ResponseEntity<Slice<ContentDetailDto>> getContentDetails(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "latest") String sortBy,
            Pageable pageable
    ) {
        Slice<ContentDetailDto> details = contentDetailService.getContentDetails(categoryId, keyword, sortBy, pageable);
        return ResponseEntity.ok(details);
    }

    //단일 콘텐츠 상세 조회(찜 여부 포함)
    @GetMapping("/{contentDetailId}")
    public ResponseEntity<ContentDetailDto> getContentDetail(@PathVariable Long contentDetailId) {
        ContentDetailDto detail = contentDetailService.getContentDetail(contentDetailId);
        return ResponseEntity.ok(detail);
    }
}
