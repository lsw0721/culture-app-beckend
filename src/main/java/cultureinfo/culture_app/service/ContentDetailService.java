package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.ContentDetail;
import cultureinfo.culture_app.domain.ContentSubcategory;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.ContentDetailCreateRequestDto;
import cultureinfo.culture_app.dto.request.ContentDetailUpdateRequestDto;
import cultureinfo.culture_app.dto.request.ContentSearchRequestDto;
import cultureinfo.culture_app.dto.response.ContentDetailDto;
import cultureinfo.culture_app.dto.response.ContentSummaryDto;
import cultureinfo.culture_app.exception.CustomException;
import cultureinfo.culture_app.exception.ErrorCode;
import cultureinfo.culture_app.repository.ContentDetailRepository;
import cultureinfo.culture_app.repository.ContentSubcategoryRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentDetailService {
    private final ContentDetailRepository contentDetailRepository;
    private final ContentSubcategoryRepository contentSubcategoryRepository;
    private final SecurityUtil securityUtil;
    private final ContentFavoriteService contentFavoriteService;
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    //콘텐츠- 관리자만 생성 가능
    @Transactional
    public ContentDetailDto createContentDetail(ContentDetailCreateRequestDto req) {
        // 1) 로그인 및 관리자 권한 확인
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        // 2) 중분류(Category) 조회
        ContentSubcategory smallCat = contentSubcategoryRepository
                .findById(req.getContentSubcategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.SMALL_CATEGORY_NOT_FOUND));

        // 3) 엔티티 빌드
        ContentDetail entity = ContentDetail.builder()
                .contentName(req.getContentName())
                .startDateTime(req.getStartDateTime())
                .endDateTime(req.getEndDateTime())
                .location(req.getLocation())
                .price(Optional.ofNullable(req.getPrice()).orElse(0L))
                .picture(null)
                .artistName(req.getArtistName())
                .sportTeamName(req.getSportTeamName())
                .brandName(req.getBrandName())
                .detailsJson(req.getDetailsJson())
                .contentSubcategory(smallCat)
                .build();

        // 4) 저장
        contentDetailRepository.save(entity);

        //이미지 업로드 처리
        MultipartFile imageFile = req.getPictureFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            //s3 업로드
            String imageUrl = s3Service.upload(imageFile, entity.getId());
            //엔티티에 url 반영
            entity.changePicture(imageUrl);
        }

        // 5) 반환용 DTO 변환 (찜 여부는 false로 초기화하거나 별도 로직)
        boolean isFav = contentFavoriteService.isFavorite(memberId, entity.getId());
        return ContentDetailDto.from(entity, isFav);

    }

    @Transactional(readOnly = true)
    //페이지 단위 콘텐츠 리스트 조회(필터, 검색, 정렬, 페이징, 찜 여부 포함)
    public Slice<ContentSummaryDto> search(ContentSearchRequestDto req){
        Long memberId = securityUtil.getCurrentId();
        return contentDetailRepository.searchContentDetails(
                req.getSubcategoryId(),
                req.getKeyword(),
                req.getArtistName(),
                req.getSportTeamName(),
                req.getBrandName(),
                req.getSortBy(),
                PageRequest.of(req.getPage(), req.getSize()), // 몇 번째 페이지에서 몇 개의 콘텐츠를 가져올지
                memberId);
    }

    //단일 콘텐츠 상세 조회
    @Transactional(readOnly = true)
    public ContentDetailDto getContentDetail(Long contentDetailId) {
        Long memberId = securityUtil.getCurrentId();
        if(memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다");
        }

        ContentDetail contentDetail = contentDetailRepository.findById(contentDetailId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠입니다."));
        boolean isFavorited = contentFavoriteService.isFavorite(memberId,contentDetailId); // 어떻게 처리할지 생각 필요
        return ContentDetailDto.from(contentDetail, isFavorited);
    }


    //콘텐츠 상세 수정 - 관리자만 수정 가능
    @Transactional
    public ContentDetailDto updateContentDetail (
            Long contentDetailId,
            ContentDetailUpdateRequestDto dto
    ) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        ContentDetail entity = contentDetailRepository.findById(contentDetailId)
                .orElseThrow(() -> new CustomException(ErrorCode.SMALL_CATEGORY_NOT_FOUND));

        //이미지 수정
        MultipartFile imageFile = dto.getPictureFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            s3Service.deleteFile(contentDetailId);
            String imageUrl = s3Service.upload(imageFile,contentDetailId);
            entity.changePicture(imageUrl);
        }

        // DTO에 담긴 값만 선택적으로 업데이트
        if (dto.getContentName() != null)
            entity.changeContentName(dto.getContentName());
        if (dto.getStartDateTime() != null|| dto.getEndDateTime() != null)
            entity.changePeriod(dto.getStartDateTime(), dto.getEndDateTime());
        if (dto.getLocation() != null)
            entity.changeLocation(dto.getLocation());

        if (dto.getArtistName() != null)
            entity.changeArtistName(dto.getArtistName());
        if (dto.getSportTeamName() != null)
            entity.changeSportTeamName(dto.getSportTeamName());
        if (dto.getBrandName() != null)
            entity.changeBrandName(dto.getBrandName());
        if (dto.getDetailsJson() != null)
            entity.changeDetailsJson(dto.getDetailsJson());

        // 변경된 엔티티를 다시 DTO로 변환해 반환
        boolean fav = contentFavoriteService.isFavorite(securityUtil.getCurrentId(), contentDetailId);
        return ContentDetailDto.from(entity, fav);
    }

    //콘텐츠 삭제
    public void deleteContentDetail(Long contentDetailId) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        if (!contentDetailRepository.existsById(contentDetailId)) {
            throw new CustomException(ErrorCode.CONTENT_NOT_FOUND);
        }

        s3Service.deleteFile(contentDetailId);
        contentDetailRepository.deleteById(contentDetailId);
    }

}
