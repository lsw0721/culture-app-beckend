package cultureinfo.culture_app.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import cultureinfo.culture_app.domain.ContentDetail;
import cultureinfo.culture_app.domain.QContentDetail;

import cultureinfo.culture_app.dto.response.ContentSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
//콘텐츠 검색 기능
public class ContentDetailRepositoryCustomImpl implements ContentDetailRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final ContentFavoriteRepository contentFavoriteRepository;
    QContentDetail contentDetail = QContentDetail.contentDetail;

    @Override
    public Slice<ContentSummaryDto> searchContentDetails(
            Long subCategoryId,
            String keyword,
            /*
            String artistName,
            String sportTeamName,
            String brandName,

             */
            String subjectName,
            String sortBy,
            Pageable pageable,
            Long memberId
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        //ContentDetail -> 소분류 -> 중분류 -> 대분류의 id가 categoryId와 일치하는 콘텐츠만 검색
        //타입 안전
        //연관관계 자동 조인

        // 키워드 기반 검색 ex) '대동제' 검색 시 '2025 동국대 대동제' 검색됨
        if (keyword!=null&&!keyword.isBlank()) builder.and(contentDetail.contentName.containsIgnoreCase(keyword));

        /*
        // 중분류 키워드 기반 검색 ex) '가수이름' 검색 시 그 가수가 포함된 콘텐츠 모두 검색됨
        if (artistName!=null&&!artistName.isBlank()) builder.and(contentDetail.artistName.containsIgnoreCase(artistName));
        if (sportTeamName!=null&&!sportTeamName.isBlank()) builder.and(contentDetail.sportTeamName.containsIgnoreCase(sportTeamName));
        if (brandName!=null&&!brandName.isBlank()) builder.and(contentDetail.brandName.containsIgnoreCase(brandName));
        */
        if (subjectName!=null&&!subjectName.isBlank()) builder.and(contentDetail.subjectName.containsIgnoreCase(subjectName));
        return fetchSlice(builder, sortBy, pageable, memberId);
    }

    //리스트별 조회
    @Override
    public Slice<ContentSummaryDto> findBySubCategory(
            Long subCategoryId, String sortBy, Pageable pageable, Long memberId
    ){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(contentDetail.contentSubcategory.id.eq(subCategoryId));
        return fetchSlice(builder, sortBy, pageable, memberId);
    }

    private Slice<ContentSummaryDto> fetchSlice(
            BooleanBuilder builder, String sortBy, Pageable pageable, Long memberId
    ){
        //정렬 기준 설정
        //프론트에선 디폴트 값으로 변경할 때는 sortBy 파라미터를 제거하는 식으로 변경
        OrderSpecifier<?> orderSpecifier = switch (sortBy){
            case "favoriteCount" -> contentDetail.favoriteCount.desc(); // 좋아요 내림차순
            case "startDateTime" -> contentDetail.startDateTime.asc(); // 시작일순 오름차순
            default -> contentDetail.startDateTime.desc(); // 시작일수 내림차순(최근 것 부터)  기본 설정
        };

        //데이터 조회(size+1개로 hasNext 판단)
        int size = pageable.getPageSize();
        List<ContentDetail> contents = queryFactory
                .selectFrom(contentDetail)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset()) // 현재 페이지 시작 위치
                .limit(size + 1) // 한 페이지당 몇 개
                .fetch();

        boolean hasNext = contents.size() > size;
        if (hasNext) {
            contents.remove(size);
        }

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
        List<ContentSummaryDto> result = contents.stream()
                .map(c -> ContentSummaryDto.of(c))
                .toList();
        return new SliceImpl<>(result, pageable, hasNext);
    }



}

