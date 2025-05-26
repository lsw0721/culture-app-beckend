package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.ArticleLike;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.response.ArticleLikeDto;
import cultureinfo.culture_app.exception.CustomException;
import cultureinfo.culture_app.exception.ErrorCode;
import cultureinfo.culture_app.repository.ArticleLikeRepository;
import cultureinfo.culture_app.repository.ArticleRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.security.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ArticleLikeService {
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final SecurityUtil securityUtil;

    @Transactional
    public ArticleLikeDto toggleLike(Long articleId) {
        Long memberId = securityUtil.getCurrentId();
        if (memberId == null) {
            throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        }
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        boolean liked;

        if (articleLikeRepository.existsByArticleIdAndMemberId(articleId, memberId)) {
            ArticleLike existing = articleLikeRepository
                    .findByArticleIdAndMemberId(articleId, memberId)
                    .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_LIKE_NOT_FOUND));
            articleLikeRepository.delete(existing);
            article.decreaseLikeCount();
            liked = false;
        } else {
            ArticleLike like = ArticleLike.builder()
                    .article(article)
                    .member(member)
                    .build();
            articleLikeRepository.save(like);
            article.increaseLikeCount();
            liked = true;
        }

        return new ArticleLikeDto(liked, article.getLikeCount());
    }
}
