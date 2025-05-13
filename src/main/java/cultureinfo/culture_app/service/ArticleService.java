package cultureinfo.culture_app.service;

import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.dto.request.ArticleRequestDto;
import cultureinfo.culture_app.dto.response.ArticleDto;
import cultureinfo.culture_app.repository.ArticleRepository;
import cultureinfo.culture_app.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    //게시글 작성
    @Transactional
    public ArticleDto createArticle(ArticleRequestDto request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Article article = Article.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .category(request.getCategory())
                .member(member)
                .build();

        return ArticleDto.from(articleRepository.save(article));
    }

    // 단건 조회
    public ArticleDto getArticle(Long id) {
        return ArticleDto.from(articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다.")));
    }

    // 전체 조회
    public List<ArticleDto> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(ArticleDto::from)
                .collect(Collectors.toList());
    }

    // 수정
    @Transactional
    public ArticleDto updateArticle(Long id, ArticleRequestDto request) {
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
