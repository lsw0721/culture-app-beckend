package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.ContentDetail;
import cultureinfo.culture_app.domain.ContentSession;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.ContentSessionCreateRequestDto;
import cultureinfo.culture_app.dto.request.ContentSessionUpdateRequestDto;
import cultureinfo.culture_app.dto.response.ContentSessionDto;
import cultureinfo.culture_app.dto.response.SessionDto;
import cultureinfo.culture_app.repository.ContentDetailRepository;
import cultureinfo.culture_app.repository.ContentSessionRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentSessionService {
    private final ContentSessionRepository contentSessionRepository;
    private final ContentDetailRepository contentDetailRepository;
    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;

    //특정 콘텐츠의 모든 세션 조회
    @Transactional(readOnly = true)
    public List<ContentSessionDto> getSessionsByContent(Long contentDetailId){
        ContentDetail detail = contentDetailRepository.findById(contentDetailId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 콘텐츠입니다."));
        return detail.getSessions().stream()
                .map(s -> new ContentSessionDto(s.getId(), s.getSessionDate(), s.getInfoJson()))
                .collect(Collectors.toList());
    }

    //세션 생성 - 관리자만
    @Transactional
    public ContentSessionDto createSession(ContentSessionCreateRequestDto dto) {

        // 1) 로그인 및 관리자 권한 확인
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }

        ContentDetail detail = contentDetailRepository.findById(dto.getContentDetailId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 콘텐츠입니다."));
        ContentSession session = ContentSession.builder()
                .contentDetail(detail)
                .sessionDate(dto.getSessionDate())
                .infoJson(dto.getInfoJson())
                .build();

        contentSessionRepository.save(session);
        return new ContentSessionDto(session.getId(), session.getSessionDate(), session.getInfoJson());
    }

    @Transactional
    public ContentSessionDto updateSession(Long sessionId, ContentSessionUpdateRequestDto dto) {
        // 1) 로그인 및 관리자 권한 확인
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }

        ContentSession session = contentSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 세션입니다."));
        session.changeSessionDate(dto.getSessionDate());
        if (dto.getInfoJson() != null) {
            session.changeInfoJson(dto.getInfoJson());
        }
        return new ContentSessionDto(session.getId(), session.getSessionDate(), session.getInfoJson());
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        // 1) 로그인 및 관리자 권한 확인
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }

        if (!contentSessionRepository.existsById(sessionId)) {
            throw new EntityNotFoundException("존재하지 않는 세션입니다.");
        }
        contentSessionRepository.deleteById(sessionId);
    }

}
