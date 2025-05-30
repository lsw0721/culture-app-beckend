package cultureinfo.culture_app.service;
import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.ContentSubCategory;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.ArticleRequestDto;
import cultureinfo.culture_app.dto.request.ArticleUpdateDto;
import cultureinfo.culture_app.dto.response.ArticleDto;
import cultureinfo.culture_app.dto.response.ArticleSummaryDto;
import cultureinfo.culture_app.exception.CustomException;
import cultureinfo.culture_app.exception.ErrorCode;
import cultureinfo.culture_app.repository.ArticleRepository;
import cultureinfo.culture_app.repository.ContentSubCategoryRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ContentSubCategoryRepository subCategoryRepository;
    private final SecurityUtil securityUtil;

    //게시글 작성: 로그인한 사용자만 가능
    @Transactional
    public ArticleDto createArticle(ArticleRequestDto request) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ContentSubCategory subCategory = subCategoryRepository.findById(request.getSubCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        Article article = Article.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .category(request.getCategory())
                .member(member)
                .subCategory(subCategory)
                .createBy(member.getUsername())
                .createDate(LocalDateTime.now())
                .build();

        return ArticleDto.from(articleRepository.save(article));
    }

    @Transactional(readOnly = true)
    // 단건 조회
    public ArticleDto getArticle(Long id) {
        return ArticleDto.from(articleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    // 전체 조회
    public Slice<ArticleSummaryDto> getAllArticles(Pageable pageable) {
        // Slice<Article>을 리포지토리에서 받아오고
        Slice<Article> slice = articleRepository.findAllBy(pageable);
        // DTO 로 매핑한 Slice 반환
        return slice.map(ArticleSummaryDto::from);
    }

    //검색(제목 or 본문의 일부 내용 입력 시 검색 가능)
    @Transactional(readOnly = true)
    public Slice<ArticleSummaryDto> searchArticles(String keyword, Pageable pageable) {
        Slice<Article> slice = articleRepository.searchByKeyword(keyword, pageable);
        // DTO로 매핑한 Slice 반환
        return slice.map(ArticleSummaryDto::from);
    }



    // 수정: 작성자 또는 ADMIN만 가능
    @Transactional
    public ArticleDto updateArticle(Long articleId, ArticleUpdateDto request) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean isOwner = article.getMember().getId().equals(memberId);
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isOwner && !isAdmin) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MODIFICATION);
        }

        article.update(request.getTitle(), request.getBody(), request.getCategory());
        article.setLastModifiedBy(article.getMember().getUsername());
        article.setLastModifiedDate(LocalDateTime.now());

        return ArticleDto.from(article);
    }

    // 삭제: 작성자 또는 ADMIN만 가능
    @Transactional
    public void deleteArticle(Long articleId) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean isOwner = article.getMember().getId().equals(memberId);
        boolean isAdmin = member.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isOwner && !isAdmin) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_DELETION);
        }
        articleRepository.delete(article);
    }

    @Transactional(readOnly = true)
    // 내 게시글만 확인
    public List<ArticleDto> getMyArticles() {
        Long memberId = securityUtil.getCurrentId();
        List<Article> articles = articleRepository.findAllByMember_id(memberId);
        List<ArticleDto> result = new ArrayList<>();
        for (Article article : articles) {
            result.add(ArticleDto.from(article));
        }
        return result;
    }
}
