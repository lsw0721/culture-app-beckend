package cultureinfo.culture_app.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.QArticle;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public ArticleRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Article> searchByKeyword(String keyword) {
        QArticle article = QArticle.article;

        return queryFactory
                .selectFrom(article)
                .where(
                        article.title.containsIgnoreCase(keyword)
                                .or(article.body.containsIgnoreCase(keyword))
                )
                .orderBy(article.createDate.desc())
                .fetch();
    }
}
