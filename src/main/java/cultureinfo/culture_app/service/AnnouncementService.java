package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.domain.type.ArticleCategory;
import cultureinfo.culture_app.dto.request.AnnouncementRequestDto;
import cultureinfo.culture_app.dto.request.AnnouncementUpdateRequestDto;

import cultureinfo.culture_app.dto.response.ArticleDto;
import cultureinfo.culture_app.exception.CustomException;
import cultureinfo.culture_app.exception.ErrorCode;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
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
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        Article announcement = Article.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .category(ArticleCategory.NOTICE)
                .member(member)
                .contentDetail(null)
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
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND)));
    }

    // 수정: ADMIN만 가능
    @Transactional
    public ArticleDto updateAnnouncement(Long announcemnetId, AnnouncementUpdateRequestDto request) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Article announcement = articleRepository.findById(announcemnetId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MODIFICATION);
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
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        Article announcement = articleRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DELETION);
        }
        articleRepository.delete(announcement);
    }
    
}
