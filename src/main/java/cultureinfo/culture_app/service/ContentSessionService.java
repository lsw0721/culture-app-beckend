package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.ContentDetail;
import cultureinfo.culture_app.domain.ContentSession;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.ContentSessionCreateRequestDto;
import cultureinfo.culture_app.dto.request.ContentSessionUpdateRequestDto;
import cultureinfo.culture_app.dto.response.ContentSessionDto;
import cultureinfo.culture_app.exception.CustomException;
import cultureinfo.culture_app.exception.ErrorCode;
import cultureinfo.culture_app.repository.ContentDetailRepository;
import cultureinfo.culture_app.repository.ContentSessionRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentSessionService {
    private final ContentSessionRepository contentSessionRepository;
    private final ContentDetailRepository contentDetailRepository;
    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;
    private final S3Service s3Service;  // S3Service 주입

    //특정 콘텐츠의 모든 세션 조회
    @Transactional(readOnly = true)
    public List<ContentSessionDto> getSessionsByContent(Long contentDetailId){
        ContentDetail detail = contentDetailRepository.findById(contentDetailId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
        return detail.getSessions().stream()
                .map(ContentSessionDto::from)
                .collect(Collectors.toList());
    }

    //세션 생성 - 관리자만
    @Transactional
    public ContentSessionDto createSession(ContentSessionCreateRequestDto dto) {

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

        ContentDetail detail = contentDetailRepository.findById(dto.getContentDetailId())
                .orElseThrow(() -> new CustomException(ErrorCode.CONTENT_NOT_FOUND));
        ContentSession session = ContentSession.builder()
                .contentDetail(detail)
                .sessionDate(dto.getSessionDate())
                .booths(dto.getBooths())
                .artistNames(dto.getArtistNames())
                .build();

        contentSessionRepository.save(session);

        return ContentSessionDto.from(session);
    }

    @Transactional
    public ContentSessionDto updateSession(Long sessionId, ContentSessionUpdateRequestDto dto) {
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
            throw new CustomException(ErrorCode.UNAUTHORIZED_MODIFICATION);
        }

        ContentSession session = contentSessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        session.changeSessionDate(dto.getSessionDate());
        session.changeBooths(dto.getBooths());
        session.changeArtistNames(dto.getArtistNames());

        return ContentSessionDto.from(session);
    }

    @Transactional
    public void deleteSession(Long sessionId) {
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
            throw new CustomException(ErrorCode.UNAUTHORIZED_DELETION);
        }

        if (!contentSessionRepository.existsById(sessionId)) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }

        contentSessionRepository.deleteById(sessionId);
    }

}
