//package cultureinfo.culture_app.service;
//
//import cultureinfo.culture_app.domain.ContentDetail;
//import cultureinfo.culture_app.domain.ContentSmallCategory;
//import cultureinfo.culture_app.domain.Member;
//import cultureinfo.culture_app.dto.request.ContentDetailCreateRequestDto;
//import cultureinfo.culture_app.dto.request.ContentDetailUpdateRequestDto;
//import cultureinfo.culture_app.dto.response.ContentDetailDto;
//import cultureinfo.culture_app.exception.CustomException;
//import cultureinfo.culture_app.exception.ErrorCode;
//import cultureinfo.culture_app.repository.ContentDetailRepository;
//import cultureinfo.culture_app.repository.ContentSmallCategoryRepository;
//import cultureinfo.culture_app.repository.MemberRepository;
//import cultureinfo.culture_app.security.SecurityUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.assertj.core.api.AssertionsForClassTypes.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.mock;
//
//@ExtendWith(MockitoExtension.class)
//class ContentDetailServiceTest {
//
//    @Mock ContentDetailRepository contentDetailRepository;
//    @Mock ContentSmallCategoryRepository contentSmallCategoryRepository;
//    @Mock MemberRepository memberRepository;
//    @Mock SecurityUtil securityUtil;
//    @Mock ContentFavoriteService contentFavoriteService;
//    @Mock S3Service s3Service;
//
//    @InjectMocks
//    ContentDetailService service;
//
//    // 공통 요청 DTO 예시
//    private ContentDetailCreateRequestDto createReq;
//    private LocalDateTime start = LocalDateTime.of(2025,5,30,10,0);
//    private LocalDateTime end   = LocalDateTime.of(2025,5,30,12,0);
//
//    @BeforeEach
//    void setUp() {
//        createReq = new ContentDetailCreateRequestDto(
//                100L,                // contentSmallCategoryId
//                "테스트콘텐츠",       // contentName
//                start, end,
//                "서울",              // location
//                5000L,               // price
//                null,                // pictureFile (없음)
//                "아티스트",          // artistName
//                "팀명",             // sportTeamName
//                "브랜드",            // brandName
//                "{\"key\":\"val\"}"  // detailsJson
//        );
//
//    }
//
//    @Test
//    void createContentDetail_Admin_정상_저장() {
//        // given
//        given(securityUtil.getCurrentId()).willReturn(1L);
//        // 멤버를 관리자로 세팅
//        Member admin = mock(Member.class);
//        var adminRole = mock(cultureinfo.culture_app.domain.type.Role.class);
//        given(adminRole.getAuthority()).willReturn("ROLE_ADMIN");
//        given(admin.getRoles()).willReturn(Set.of(adminRole));
//        given(memberRepository.findById(1L)).willReturn(Optional.of(admin));
//
//        // 소분류 조회 성공
//        ContentSmallCategory smallCat = mock(ContentSmallCategory.class);
//        given(contentSmallCategoryRepository.findById(100L))
//                .willReturn(Optional.of(smallCat));
//
//        // 찜 여부 반환
//        given(contentFavoriteService.isFavorite(1L, null)).willReturn(false);
//
//        // when
//        ContentDetailDto dto = service.createContentDetail(createReq);
//
//        // then
//        then(contentDetailRepository).should()
//                .save(ArgumentMatchers.any(ContentDetail.class));
//        then(contentFavoriteService).should()
//                .isFavorite(1L, null);
//        assertThat(dto.getContentName()).isEqualTo("테스트콘텐츠");
//        assertThat(dto.isFavorite()).isFalse();
//    }
//
//    @Test
//    void createContentDetail_비로그인시_LOGIN_REQUIRED() {
//        // given
//        given(securityUtil.getCurrentId()).willReturn(null);
//
//        // when / then
//        CustomException ex = catchThrowableOfType(
//                () -> service.createContentDetail(createReq),
//                CustomException.class
//        );
//        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.LOGIN_REQUIRED);
//    }
//
//    @Test
//    void createContentDetail_관리자아닐시_ACCESS_DENIED() {
//        // given
//        given(securityUtil.getCurrentId()).willReturn(2L);
//        // 일반 사용자 멤버
//        Member user = mock(Member.class);
//        var userRole = mock(cultureinfo.culture_app.domain.type.Role.class);
//        given(userRole.getAuthority()).willReturn("ROLE_USER");
//        given(user.getRoles()).willReturn(Set.of(userRole));
//        given(memberRepository.findById(2L)).willReturn(Optional.of(user));
//
//        // when / then
//        CustomException ex = catchThrowableOfType(
//                () -> service.createContentDetail(createReq),
//                CustomException.class
//        );
//        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ACCESS_DENIED);
//    }
//
//    @Test
//    void createContentDetail_소분류없을때_SMALL_CATEGORY_NOT_FOUND() {
//        // given
//        given(securityUtil.getCurrentId()).willReturn(1L);
//        Member admin = mock(Member.class);
//        var adminRole = mock(cultureinfo.culture_app.domain.type.Role.class);
//        given(adminRole.getAuthority()).willReturn("ROLE_ADMIN");
//        given(admin.getRoles()).willReturn(Set.of(adminRole));
//        given(memberRepository.findById(1L)).willReturn(Optional.of(admin));
//
//        given(contentSmallCategoryRepository.findById(100L))
//                .willReturn(Optional.empty());
//
//        // when / then
//        CustomException ex = catchThrowableOfType(
//                () -> service.createContentDetail(createReq),
//                CustomException.class
//        );
//        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SMALL_CATEGORY_NOT_FOUND);
//    }
//
//    @Test
//    void getContentDetail_정상조회() {
//        // given
//        given(securityUtil.getCurrentId()).willReturn(1L);
//
//        ContentDetail detail = mock(ContentDetail.class);
//        given(contentDetailRepository.findById(200L))
//                .willReturn(Optional.of(detail));
//        given(contentFavoriteService.isFavorite(1L, 200L))
//                .willReturn(true);
//
//        // when
//        ContentDetailDto dto = service.getContentDetail(200L);
//
//        // then
//        then(contentDetailRepository).should().findById(200L);
//        assertThat(dto.isFavorite()).isTrue();
//    }
//
//    @Test
//    void getContentDetail_비로그인시_AccessDeniedException() {
//        // given
//        given(securityUtil.getCurrentId()).willReturn(null);
//
//        // when / then
//        assertThatThrownBy(() -> service.getContentDetail(200L))
//                .isInstanceOf(org.springframework.security.access.AccessDeniedException.class)
//                .hasMessage("로그인이 필요합니다");
//    }
//
//
//
//    @Test
//    void updateContentDetail_Admin_정상_수정() {
//        // given: 관리자로 로그인
//        given(securityUtil.getCurrentId()).willReturn(1L);
//        Member admin = mock(Member.class);
//        var adminRole = mock(cultureinfo.culture_app.domain.type.Role.class);
//        given(adminRole.getAuthority()).willReturn("ROLE_ADMIN");
//        given(admin.getRoles()).willReturn(Set.of(adminRole));
//        given(memberRepository.findById(1L)).willReturn(Optional.of(admin));
//
//        // 기존 엔티티 조회
//        ContentDetail entity = mock(ContentDetail.class);
//        given(contentDetailRepository.findById(300L))
//                .willReturn(Optional.of(entity));
//
//        // 찜 여부 조회
//        given(contentFavoriteService.isFavorite(1L, 300L)).willReturn(true);
//
//        // update 요청 DTO 준비
//        LocalDateTime newStart = start.plusDays(1);
//        LocalDateTime newEnd   = end.plusDays(1);
//        ContentDetailUpdateRequestDto updateReq = new ContentDetailUpdateRequestDto(
//                null,
//                "수정테스트",
//                newStart,
//                newEnd,
//                null,
//                null,
//                "부산",
//                null,
//                "아티스트2",
//                "팀2",
//                "브랜드2",
//                "{\"updated\":\"true\"}"
//        );
//
//        // when
//        ContentDetailDto result = service.updateContentDetail(300L, updateReq);
//
//        // then
//        then(contentDetailRepository).should().findById(300L);
//        // 필요한 엔티티 변경 메서드 호출 검증 (예: changeContentName)
//        then(entity).should().changeContentName("수정테스트");
//        then(entity).should().changePeriod(newStart, newEnd);
//        then(entity).should().changeLocation("부산");
//        then(entity).should().changeArtistName("아티스트2");
//        then(entity).should().changeSportTeamName("팀2");
//        then(entity).should().changeBrandName("브랜드2");
//        then(entity).should().changeDetailsJson("{\"updated\":\"true\"}");
//        assertThat(result.isFavorite()).isTrue();
//    }
//
//    @Test
//    void updateContentDetail_비로그인시_LOGIN_REQUIRED() {
//        given(securityUtil.getCurrentId()).willReturn(null);
//
//        ContentDetailUpdateRequestDto anyReq = mock(ContentDetailUpdateRequestDto.class);
//        CustomException ex = catchThrowableOfType(
//                () -> service.updateContentDetail(300L, anyReq),
//                CustomException.class
//        );
//        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.LOGIN_REQUIRED);
//    }
//
//    @Test
//    void updateContentDetail_관리자아닌_사용자_ACCESS_DENIED() {
//        given(securityUtil.getCurrentId()).willReturn(2L);
//        Member user = mock(Member.class);
//        var userRole = mock(cultureinfo.culture_app.domain.type.Role.class);
//        given(userRole.getAuthority()).willReturn("ROLE_USER");
//        given(user.getRoles()).willReturn(Set.of(userRole));
//        given(memberRepository.findById(2L)).willReturn(Optional.of(user));
//
//        ContentDetailUpdateRequestDto anyReq = mock(ContentDetailUpdateRequestDto.class);
//        CustomException ex = catchThrowableOfType(
//                () -> service.updateContentDetail(300L, anyReq),
//                CustomException.class
//        );
//        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ACCESS_DENIED);
//    }
//
//    @Test
//    void updateContentDetail_엔티티없음_SMALL_CATEGORY_NOT_FOUND() {
//        given(securityUtil.getCurrentId()).willReturn(1L);
//        Member admin = mock(Member.class);
//        var adminRole = mock(cultureinfo.culture_app.domain.type.Role.class);
//        given(adminRole.getAuthority()).willReturn("ROLE_ADMIN");
//        given(admin.getRoles()).willReturn(Set.of(adminRole));
//        given(memberRepository.findById(1L)).willReturn(Optional.of(admin));
//
//        given(contentDetailRepository.findById(300L)).willReturn(Optional.empty());
//
//        ContentDetailUpdateRequestDto anyReq = mock(ContentDetailUpdateRequestDto.class);
//        CustomException ex = catchThrowableOfType(
//                () -> service.updateContentDetail(300L, anyReq),
//                CustomException.class
//        );
//        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SMALL_CATEGORY_NOT_FOUND);
//    }
//
//    @Test
//    void deleteContentDetail_Admin_정상_삭제() {
//        given(securityUtil.getCurrentId()).willReturn(1L);
//        Member admin = mock(Member.class);
//        var adminRole = mock(cultureinfo.culture_app.domain.type.Role.class);
//        given(adminRole.getAuthority()).willReturn("ROLE_ADMIN");
//        given(admin.getRoles()).willReturn(Set.of(adminRole));
//        given(memberRepository.findById(1L)).willReturn(Optional.of(admin));
//
//        given(contentDetailRepository.existsById(400L)).willReturn(true);
//
//        // when
//        service.deleteContentDetail(400L);
//
//        // then
//        then(s3Service).should().deleteFile(400L);
//        then(contentDetailRepository).should().deleteById(400L);
//    }
//
//    @Test
//    void deleteContentDetail_비로그인시_LOGIN_REQUIRED() {
//        given(securityUtil.getCurrentId()).willReturn(null);
//
//        CustomException ex = catchThrowableOfType(
//                () -> service.deleteContentDetail(400L),
//                CustomException.class
//        );
//        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.LOGIN_REQUIRED);
//    }
//
//    @Test
//    void deleteContentDetail_관리자아닌_사용자_ACCESS_DENIED() {
//        given(securityUtil.getCurrentId()).willReturn(2L);
//        Member user = mock(Member.class);
//        var userRole = mock(cultureinfo.culture_app.domain.type.Role.class);
//        given(userRole.getAuthority()).willReturn("ROLE_USER");
//        given(user.getRoles()).willReturn(Set.of(userRole));
//        given(memberRepository.findById(2L)).willReturn(Optional.of(user));
//
//        CustomException ex = catchThrowableOfType(
//                () -> service.deleteContentDetail(400L),
//                CustomException.class
//        );
//        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ACCESS_DENIED);
//    }
//
//    @Test
//    void deleteContentDetail_존재하지않는콘텐츠_CONTENT_NOT_FOUND() {
//        given(securityUtil.getCurrentId()).willReturn(1L);
//        Member admin = mock(Member.class);
//        var adminRole = mock(cultureinfo.culture_app.domain.type.Role.class);
//        given(adminRole.getAuthority()).willReturn("ROLE_ADMIN");
//        given(admin.getRoles()).willReturn(Set.of(adminRole));
//        given(memberRepository.findById(1L)).willReturn(Optional.of(admin));
//
//        given(contentDetailRepository.existsById(400L)).willReturn(false);
//
//        CustomException ex = catchThrowableOfType(
//                () -> service.deleteContentDetail(400L),
//                CustomException.class
//        );
//        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CONTENT_NOT_FOUND);
//    }
//}