package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.ContentDetail;
import cultureinfo.culture_app.dto.response.ContentDetailDto;
import cultureinfo.culture_app.dto.response.ContentFavoriteDto;
import cultureinfo.culture_app.repository.ContentDetailRepository;
import cultureinfo.culture_app.repository.ContentFavoriteRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentDetailService {
    private final ContentDetailRepository contentDetailRepository;
    private final SecurityUtil securityUtil;
    private final ContentFavoriteRepository contentFavoriteRepository;
    private final ContentFavoriteService contentFavoriteService;

    @Transactional(readOnly = true)
    //페이지 단위 콘텐츠 리스트 조회(필터, 검색, 정렬, 페이징, 찜 여부 포함)
    public Page<ContentDetailDto> getContentDetails(
            Long categoryId,
            String keyword,
            String sortBy,
            Pageable pageable
    ){
        Long memberId = getMemberIdOrNull();
        return contentDetailRepository.searchContentDetails(
                categoryId, keyword, sortBy, pageable, memberId
        );
    }
    //단일 콘텐츠 상세 조회
    @Transactional(readOnly = true)
    public ContentDetailDto getContentDetail(Long contentDetailId) {
        Long memberId = getMemberIdOrNull();
        ContentDetail contentDetail = contentDetailRepository.findById(contentDetailId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠입니다."));
        boolean isFavorited = (memberId != null)
                && contentFavoriteService.isFavorite(contentDetailId);
        return ContentDetailDto.from(contentDetail, isFavorited);

    }

    //찜 토글 후 최신 상태의 dto 리턴
    public ContentFavoriteDto toggleFavorite(Long contentDetailId) {
        Long memberId = getMemberIdOrNull();
        if (memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }
        //토글 로직 수행(true = 찜, false = 찜 해제)
        return contentFavoriteService.toggleFavorite(contentDetailId);
    }

    /** 로그인 사용자 ID 반환, 인증 정보 없으면 null */
    private Long getMemberIdOrNull() {
        try {
            return securityUtil.getCurrentId();
        } catch (RuntimeException e) {
            return null;
        }
    }

}
