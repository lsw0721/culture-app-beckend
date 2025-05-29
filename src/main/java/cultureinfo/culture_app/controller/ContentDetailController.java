package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.dto.request.ContentDetailCreateRequestDto;
import cultureinfo.culture_app.dto.request.ContentDetailUpdateRequestDto;
import cultureinfo.culture_app.dto.request.ContentListRequestDto;
import cultureinfo.culture_app.dto.request.ContentSearchRequestDto;
import cultureinfo.culture_app.dto.response.ContentCategoryDto;
import cultureinfo.culture_app.dto.response.ContentDetailDto;
import cultureinfo.culture_app.dto.response.ContentSummaryDto;
import cultureinfo.culture_app.dto.response.SubCategoryDto;
import cultureinfo.culture_app.service.ContentDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentDetailController {

    private final ContentDetailService contentDetailService;

    // 목록 조회: 대분류(main), 중분류(sub), 소분류(small) 모두 옵션으로 받아 필터링

    @GetMapping
    public Slice<ContentSummaryDto> search(@Valid ContentSearchRequestDto req) {
        return contentDetailService.searchByKeyword(req);
    }

    /** 2. 대분류 전체 조회
     * GET /api/contents/categories
     */
    @GetMapping("/categories")
    public List<ContentCategoryDto> getAllCategories() {
        return contentDetailService.getAllContentCategories();
    }

    /** 3. 특정 대분류의 중분류 조회
     * GET /api/contents/categories/{categoryId}/subcategories
     */
    @GetMapping("/categories/{categoryId}/subcategories")
    public List<SubCategoryDto> getSubCategories(
            @PathVariable Long categoryId
    ) {
        return contentDetailService.getAllSubCategories(categoryId);
    }

    /** 4. 특정 중분류 기준 콘텐츠 리스트업
     * GET /api/contents/subcategories/{subCategoryId}/contents?page=&size=&sortBy=
     */
    @GetMapping("/subcategories/{subCategoryId}/contents")
    public Slice<ContentSummaryDto> listBySubCategory(
            @PathVariable Long subCategoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "startDateTime") String sortBy
    ) {
        ContentListRequestDto req = ContentListRequestDto.builder()
                .subCategoryId(subCategoryId)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();
        return contentDetailService.listBySubCategory(req);
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
