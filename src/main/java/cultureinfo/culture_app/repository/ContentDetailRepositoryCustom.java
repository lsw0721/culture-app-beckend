package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.dto.response.ContentSummaryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ContentDetailRepositoryCustom {
    Slice<ContentSummaryDto> searchContentDetails(
            Long smallCategoryId,
            String keyword,
            String artistName,
            String sportTeamName,
            String brandName,
            String sortBy,
            Pageable pageable,
            Long memberId
    );
}
