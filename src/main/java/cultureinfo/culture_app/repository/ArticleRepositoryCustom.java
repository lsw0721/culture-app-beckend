package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

//querydsl을 사용하기 위한 커스텀 저장소 정의
public interface ArticleRepositoryCustom {
    //키워드 검색 + Slice(무한 스크롤용)
    Slice<Article> searchByKeyword(String keyword, Pageable pageable);

    //slice 적용한 전체 게시글 조회
    Slice<Article> findAllBy(Pageable pageable);
}
