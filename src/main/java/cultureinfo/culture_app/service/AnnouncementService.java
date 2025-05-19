package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.domain.type.ArticleCategory;
import cultureinfo.culture_app.dto.request.AnnouncementRequestDto;
import cultureinfo.culture_app.dto.request.AnnouncementUpdateDto;

import cultureinfo.culture_app.dto.response.ArticleDto;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementService {

    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;
    private final ArticleRepository articleRepository;

    //공지사항 작성: Admin만
    @Transactional
    public ArticleDto createAnnouncement(AnnouncementRequestDto request) {
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

        Article announcement = Article.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .category(ArticleCategory.NOTICE)
                .member(member)
                .content(null)
                .createBy(member.getUsername())
                .createDate(LocalDateTime.now())
                .build();

        return ArticleDto.from(articleRepository.save(announcement));
    }



    //전체 조회
    @Transactional(readOnly = true)
    public List<ArticleDto> getAllAnnouncements() {
        List<Article> announcements = articleRepository.findAllByCategory(ArticleCategory.NOTICE);

        List<ArticleDto> result = new ArrayList<>();
        for (Article announcement : announcements) {
            result.add(ArticleDto.from(announcement));
        }
        return result;
    }

    // 단건 조회
    @Transactional(readOnly = true)
    public ArticleDto getAnnouncement(Long id) {
        return ArticleDto.from(articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("공지사항이 존재하지 않습니다.")));
    }

    // 수정: ADMIN만 가능
    @Transactional
    public ArticleDto updateAnnouncement(Long announcemnetId, AnnouncementUpdateDto request) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }
        Article announcement = articleRepository.findById(announcemnetId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("글 수정 권한이 없습니다.");
        }
        announcement.update(request.getTitle(), request.getBody(), ArticleCategory.NOTICE);
        announcement.setLastModifiedBy(announcement.getMember().getUsername());
        announcement.setLastModifiedDate(LocalDateTime.now());
        
        return ArticleDto.from(announcement);
    }
    
    // 삭제: ADMIN만 가능
    @Transactional
    public void deleteAnnouncement(Long announcementId) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }

        Article announcement = articleRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("글 삭제 권한이 없습니다.");
        }
        articleRepository.delete(announcement);
    }
    
}
