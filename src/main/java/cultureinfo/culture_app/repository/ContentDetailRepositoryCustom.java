package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.dto.response.ContentDetailDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ContentDetailRepositoryCustom {
    Slice<ContentDetailDto> searchContentDetails(
            Long categoryId, String keyword, String sortBy, Pageable pageable, Long memberId
    );
}
