package cultureinfo.culture_app.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.QArticle;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

//커스텀 저장소 구현
//페이징 기법 적용 필요
@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //게시글 검색
    @Override
    public List<Article> searchByKeyword(String keyword) {
        QArticle article = QArticle.article;

        return queryFactory
                .selectFrom(article) // article 테이블 전체 선택
                .where(
                        article.title.containsIgnoreCase(keyword) // 제목 부분일치, 대소문자 무시
                                .or(article.body.containsIgnoreCase(keyword)) // 본문 부분일치, 대소문자 무시
                )
                .orderBy(article.createDate.desc()) // 최신순 정령 생성일자 기준 내림차
                .fetch(); // 결과 반환
    }
}
