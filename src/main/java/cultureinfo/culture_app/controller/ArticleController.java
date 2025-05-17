package cultureinfo.culture_app.controller;

import cultureinfo.culture_app.dto.request.ArticleRequestDto;
import cultureinfo.culture_app.dto.request.ArticleUpdateDto;
import cultureinfo.culture_app.dto.response.ArticleDto;
import cultureinfo.culture_app.dto.response.ArticleSummaryDto;
import cultureinfo.culture_app.service.ArticleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import cultureinfo.culture_app.security.SecurityUtil;
import cultureinfo.culture_app.repository.MemberRepository;
import cultureinfo.culture_app.domain.Member;
import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.repository.ArticleRepository;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final SecurityUtil securityUtil;
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    //게시글 생성
    @PostMapping
    public ResponseEntity<ArticleDto> createArticle(
            @RequestBody ArticleRequestDto requestDto) {

        //현재 로그인한 사용자의 id를 가져오는 함수
        Long memberId = securityUtil.getCurrentId();

        ArticleDto created = articleService.createArticle(requestDto, memberId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }
    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticle(@PathVariable Long id) {
        ArticleDto article = articleService.getArticle(id);
        return ResponseEntity.ok(article);
    }

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        List<ArticleDto> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    //게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<ArticleSummaryDto>> searchArticles(@RequestParam String keyword){
        List<ArticleSummaryDto> articles = articleService.searchArticles(keyword);
        return ResponseEntity.ok(articles);
    }


    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDto> updateArticle(
            @PathVariable Long id,
            @RequestBody ArticleUpdateDto requestDto) {

        //현재 로그인한 사용자의 id를 가져오는 함수
        Long memberId = securityUtil.getCurrentId();

        //가져온 id 정보로 user 정보 가져오기
        Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Article article = articleRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("글이 존재하지 않습니다."));

        String role = member.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        //admin이거나 본인일 때 수정가능하게 설정
        if(!(article.getMember().getId() == memberId) && role =="ROLE_USER"){
                throw new AccessDeniedException("본인의 글만 수정할 수 있습니다.");
        }

        ArticleDto updated = articleService.updateArticle(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {

        //현재 로그인한 사용자의 id를 가져오는 함수
        Long memberId = securityUtil.getCurrentId();

        //가져온 id 정보로 user 정보 가져오기
        Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        Article article = articleRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("글이 존재하지 않습니다."));

        String role = member.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        //admin이거나 본인일 때 삭제가능하게 설정
        if(!(article.getMember().getId() == memberId) && role =="ROLE_USER"){
                throw new AccessDeniedException("본인의 글만 수정할 수 있습니다.");
        }

        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
