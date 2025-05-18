package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.AnnouncementRequestDto;
import cultureinfo.culture_app.dto.request.AnnouncementUpdateDto;
import cultureinfo.culture_app.dto.response.AnnouncementDto;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cultureinfo.culture_app.domain.Announcement;
import cultureinfo.culture_app.repository.AnnouncementRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementService {

    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;
    private final AnnouncementRepository announcementRepository;

    //공지사항 작성: Admin만
    @Transactional
    public AnnouncementDto createAnnouncement(AnnouncementRequestDto request) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("글 작성 권한이 없습니다.");
        }

        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .createDate(LocalDateTime.now())
                .build();

        return AnnouncementDto.from(announcementRepository.save(announcement));
    }



    //전체 조회
    @Transactional(readOnly = true)
    public List<AnnouncementDto> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();

        List<AnnouncementDto> result = new ArrayList<>();
        for (Announcement announcement : announcements) {
            result.add(AnnouncementDto.from(announcement));
        }
        return result;
    }

    // 단건 조회
    @Transactional(readOnly = true)
    public AnnouncementDto getAnnouncement(Long id) {
        return AnnouncementDto.from(announcementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("공지사항이 존재하지 않습니다.")));
    }

    // 수정: ADMIN만 가능
    @Transactional
    public AnnouncementDto updateAnnouncement(Long announcemnetId, AnnouncementUpdateDto request) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }
        Announcement announcement = announcementRepository.findById(announcemnetId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("글 수정 권한이 없습니다.");
        }
        announcement.update(request.getTitle(), request.getBody());


        return AnnouncementDto.from(announcement);
    }
    
    // 삭제: ADMIN만 가능
    @Transactional
    public void deleteAnnouncement(Long announcementId) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("글 삭제 권한이 없습니다.");
        }
        announcementRepository.delete(announcement);
    }
    
}
