package cultureinfo.culture_app.repository;

import cultureinfo.culture_app.domain.Article;

import java.util.List;

//querydsl을 사용하기 위한 커스텀 저장소 정의
public interface ArticleRepositoryCustom {
    List<Article> searchByKeyword(String keyword);
}
