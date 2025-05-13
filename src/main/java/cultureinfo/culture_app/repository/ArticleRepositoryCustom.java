package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.Article;

import java.util.List;

//querydsl 사용
// 게시글 검색 리포지토리
public interface ArticleRepositoryCustom {
    List<Article> searchByKeyword(String keyword);
}
