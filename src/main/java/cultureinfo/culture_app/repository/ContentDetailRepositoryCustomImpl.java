package cultureinfo.culture_app.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cultureinfo.culture_app.domain.ContentDetail;
import cultureinfo.culture_app.domain.QContentDetail;
import cultureinfo.culture_app.dto.response.ContentDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class ContentDetailRepositoryCustomImpl implements ContentDetailRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final ContentFavoriteRepository contentFavoriteRepository;

    @Override
    public Page<ContentDetailDto> searchContentDetails(
            Long categoryId, String keyword, String sortBy, Pageable pageable,
            Long memberId ) {
        QContentDetail contentDetail = QContentDetail.contentDetail;
        BooleanBuilder builder = new BooleanBuilder();

        //ContentDetail -> 소분류 -> 중분류 -> 대분류의 id가 categoryId와 같은 콘텐츠만 검색
        //타입 안전
        //연관관계 자동 조인

        //필터링 - 카테고리
        if(categoryId != null) {
            builder.and(contentDetail
                    .contentSmallCategory
                    .contentSubcategory
                    .contentCategory
                    .id.eq(categoryId));
        }

        //필터링 - 키워드 기반 검색 ex) '대동제' 검색 시 '2025 동국대 대동제' 검색됨
        if(keyword != null && !keyword.isBlank()) {
            builder.and(contentDetail
                    .contentName
                    .containsIgnoreCase(keyword));
        }

        //정렬 기준 설정
        OrderSpecifier<?> orderSpecifier = switch (sortBy){
            case "favoriteCount" -> contentDetail.favoriteCount.desc(); // 좋아요 내림차순
            case "startDateTime" -> contentDetail.startDateTime.asc(); // 시작일순 오름차순
            default -> contentDetail.startDateTime.desc(); // 시작일수 내림차순(최근 것 부터)
        };

        //데이터 조회(페이징)
        List<ContentDetail> contents = queryFactory
                .selectFrom(contentDetail)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset()) // 현재 페이지 시작 위치
                .limit(pageable.getPageSize()) // 한 페이지당 몇 개
                .fetch();

        //전체 개수 조회
        long total = Optional.ofNullable(queryFactory
                .select(contentDetail.count())
                .from(contentDetail)
                .where(builder)
                .fetchOne()
        ).orElse(0L);

        //찜 여부 처리
        Set<Long> favoriteIds = new HashSet<>();
        if(memberId != null) {
            List<Long> ids = contents.stream().map(ContentDetail::getId).toList();
            favoriteIds.addAll(
                    contentFavoriteRepository.findAllByMemberIdAndContentDetailIdIn(memberId, ids)
                            .stream().map(fav -> fav.getContentDetail().getId())
                            .toList()
            );
        }

        //dto 변환 및 찜 여부 포함
        List<ContentDetailDto> result = contents.stream()
                .map(cd -> ContentDetailDto.from(cd, favoriteIds.contains(cd.getId())))
                .toList();

        return new PageImpl<>(result, pageable, total);
    }

}
