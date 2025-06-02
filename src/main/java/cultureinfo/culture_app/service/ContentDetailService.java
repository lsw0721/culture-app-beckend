package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.ContentCategory;
import cultureinfo.culture_app.domain.ContentDetail;
import cultureinfo.culture_app.domain.ContentSubCategory;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.ContentDetailCreateRequestDto;
import cultureinfo.culture_app.dto.request.ContentDetailUpdateRequestDto;
import cultureinfo.culture_app.dto.request.ContentListRequestDto;
import cultureinfo.culture_app.dto.request.ContentSearchRequestDto;
import cultureinfo.culture_app.dto.response.ContentCategoryDto;
import cultureinfo.culture_app.dto.response.ContentDetailDto;
import cultureinfo.culture_app.dto.response.ContentSummaryDto;
import cultureinfo.culture_app.dto.response.SubCategoryDto;
import cultureinfo.culture_app.exception.CustomException;
import cultureinfo.culture_app.exception.ErrorCode;
import cultureinfo.culture_app.repository.ContentCategoryRepository;
import cultureinfo.culture_app.repository.ContentDetailRepository;
import cultureinfo.culture_app.repository.ContentSubCategoryRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentDetailService {
    private final ContentDetailRepository contentDetailRepository;
    private final ContentSubCategoryRepository contentSubcategoryRepository;
    private final ContentCategoryRepository contentCategoryRepository;
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

        // 2) 중분류(Category) 검증
        ContentSubCategory smallCat = contentSubcategoryRepository
                .findById(req.getContentSubcategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 3) 엔티티 빌드
        ContentDetail entity = ContentDetail.builder()
                .contentName(req.getContentName())
                .startDateTime(req.getStartDateTime())
                .endDateTime(req.getEndDateTime())
                .location(req.getLocation())
                .address(req.getAddress())
                .price(req.getPrice())
                .imageUrls(null)
                .subjectNames(req.getSubjectNames())
                .subject(req.getSubject())
                .link(req.getLink())
                .contentSubcategory(smallCat)
                .build();

        // 4) 저장
        contentDetailRepository.save(entity);

        //썸네일 업로드 처리
        MultipartFile imageFile = req.getThumbnailFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            //s3 업로드
            String imageUrl = s3Service.upload(imageFile, entity.getId());
            //엔티티에 url 반영
            entity.addImageUrl(imageUrl);
        }

        // 세부정보 이미지 업로드 → 두 번째 이후 요소로 순서대로 URL 넣기
        List<MultipartFile> detailFiles = req.getDetailFiles(); // List<MultipartFile>
        if (detailFiles != null) {
            for (MultipartFile detailFile : detailFiles) {
                if (!detailFile.isEmpty()) {
                    // fileName 예시: "42_day1_boothA.jpg", "42_day1_boothB.jpg" 등
                    String detailUrl = s3Service.upload(detailFile, entity.getId());
                    entity.addDetailImageUrl(detailUrl);
                }
            }
        }


            // 5) 반환용 DTO 변환 (찜 여부는 false로 초기화하거나 별도 로직)
        boolean isFav = contentFavoriteService.isFavorite(memberId, entity.getId());
        return ContentDetailDto.from(entity, isFav);

    }

    //단일 콘텐츠 상세 조회
    @Transactional(readOnly = true)
    public ContentDetailDto getContentDetail(Long contentDetailId) {
        Long memberId = securityUtil.getCurrentId();
        if(memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        ContentDetail contentDetail = contentDetailRepository.findById(contentDetailId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
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
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));

        //이미지 수정
        MultipartFile imageFile = dto.getThumbnailFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            s3Service.deleteFile(contentDetailId);
            String imageUrl = s3Service.upload(imageFile,contentDetailId);
            entity.changeThumbnail(imageUrl);
        }

        List<MultipartFile> detailFiles = dto.getDetailFiles(); // List<MultipartFile>
        if (detailFiles != null) {
            entity.changeDetail();
            for (MultipartFile detailFile : detailFiles) {
                if (!detailFile.isEmpty()) {

                    String detailUrl = s3Service.upload(detailFile, entity.getId());
                    entity.addDetailImageUrl(detailUrl);
                }
            }
        }

        // DTO에 담긴 값만 선택적으로 업데이트
        if (dto.getContentName() != null)
            entity.changeContentName(dto.getContentName());
        if (dto.getStartDateTime() != null|| dto.getEndDateTime() != null)
            entity.changePeriod(dto.getStartDateTime(), dto.getEndDateTime());
        if (dto.getLocation() != null)
            entity.changeLocation(dto.getLocation());
        if (dto.getSubjectNames() != null)
            entity.changeSubject(dto.getSubject());
        if (dto.getSubjectNames() != null)
            entity.changeSubjectNames(dto.getSubjectNames());
        if(dto.getLink() != null)
            entity.changeLink(dto.getLink());
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

    //탐색 페이지

    //1. 키워드 기반 검색
    @Transactional(readOnly = true)
    //페이지 단위 콘텐츠 리스트 조회(필터, 검색, 정렬, 페이징, 찜 여부 포함)
    public Slice<ContentSummaryDto> searchByKeyword(ContentSearchRequestDto req){
        Long memberId = securityUtil.getCurrentId();

        return contentDetailRepository.searchContentDetails(
                req.getSubCategoryId(),
                req.getKeyword(),
                req.getSubjectName(),
                req.getSortBy(),
                PageRequest.of(req.getPage(), req.getSize()), // 몇 번째 페이지에서 몇 개의 콘텐츠를 가져올지
                memberId);
    }
    
    
    //2. 카테고리별 리스트업
    //대분류 조회
    @Transactional(readOnly = true)
    public List<ContentCategoryDto> getAllContentCategories() {
        return contentCategoryRepository.findAll().stream()
                .map(ContentCategoryDto::from)
                .collect(Collectors.toList());
    }

    //특정 대분류의 중분류 조회
    @Transactional(readOnly = true)
    public List<SubCategoryDto> getAllSubCategories(Long categoryId) {
        ContentCategory large = contentCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        return large.getSubcategories().stream()
                .map(SubCategoryDto::from)
                .collect(Collectors.toList());
    }
    
    //특정 중분류에 속해 있는 콘텐츠 리스트업
    @Transactional(readOnly = true)
    public Slice<ContentSummaryDto> listBySubCategory(
           ContentListRequestDto req) {
        Long memberId = securityUtil.getCurrentId();
        Pageable pg = PageRequest.of(
                req.getPage(),
                req.getSize(),
                Sort.by(req.getSortBy())
        );
        return contentDetailRepository.searchContentDetails(
                req.getSubCategoryId(),  // 중분류 ID로만
                null,                    // 스포츠 팀명 검색 없음
                null,
                req.getSortBy(),         // 정렬 기준
                pg,
                memberId
        );
    }
}
