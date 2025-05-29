package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.dto.response.ContentSummaryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

//콘텐츠 검색 / 페이징용 리포지토리
public interface ContentDetailRepositoryCustom {
    Slice<ContentSummaryDto> searchContentDetails(
            Long subCategoryId,
            String keyword,
            String artistName,
            String sportTeamName,
            String brandName,
            String sortBy,
            Pageable pageable,
            Long memberId
    );

    Slice<ContentSummaryDto> findBySubCategory(
            Long subCategoryId,
            String sortBy,
            Pageable pageable,
            Long memberId
    );
}
