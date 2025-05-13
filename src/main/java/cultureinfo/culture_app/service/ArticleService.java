package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.Content;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.ArticleRequestDto;
import cultureinfo.culture_app.dto.request.ArticleUpdateDto;
import cultureinfo.culture_app.dto.response.ArticleDto;
import cultureinfo.culture_app.dto.response.ArticleSummaryDto;
import cultureinfo.culture_app.repository.ArticleRepository;
import cultureinfo.culture_app.repository.ContentRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;

    //게시글 작성
    @Transactional
    public ArticleDto createArticle(ArticleRequestDto request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        Content content = contentRepository.findById(request.getContentId())
                .orElseThrow(() -> new EntityNotFoundException("콘텐츠가 존재하지 않습니다."));
        Article article = Article.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .category(request.getCategory())
                .member(member)
                .content(content)
                .build();

        return ArticleDto.from(articleRepository.save(article));
    }

    @Transactional(readOnly = true)
    // 단건 조회
    public ArticleDto getArticle(Long id) {
        return ArticleDto.from(articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다.")));
    }

    @Transactional(readOnly = true)
    // 전체 조회
    public List<ArticleDto> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleDto> result = new ArrayList<>();
        for (Article article : articles) {
            result.add(ArticleDto.from(article));
        }
        return result;
    }

    //검색(제목 or 본문의 일부 내용 입력 시 검색 가능)
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> searchArticles(String keyword) {
        List<Article> articles = articleRepository.searchByKeyword(keyword);
        return articles.stream()
                .map(ArticleSummaryDto::from)
                .toList();
    }

    //삭제 및 수정은 작성자만 가능하게 권한 부여 필요

    // 수정
    @Transactional
    public ArticleDto updateArticle(Long id, ArticleUpdateDto request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
        article.update(request.getTitle(), request.getBody(), request.getCategory());

        return ArticleDto.from(article);
    }

    // 삭제
    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
        articleRepository.delete(article);
    }
}
