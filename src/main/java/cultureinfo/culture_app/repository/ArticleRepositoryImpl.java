package cultureinfo.culture_app.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.QArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;


//커스텀 저장소 구현
//페이징 기법 적용 필요
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    //게시글 검색
    @Override
    public Slice<Article> searchByKeyword(Long subCategoryId, String keyword, Pageable pageable) {
        QArticle article = QArticle.article;

        //실제로 조회할 개수 + 1 (hasNext 판별용)
        int limit = pageable.getPageSize() + 1;

        List<Article> fetched = queryFactory
                .selectFrom(article)
                .where(
                        article.subCategory.id.eq(article.subCategory.id)
                                .and(//제목에 키워드가 있거나
                                        article.title.containsIgnoreCase(keyword)
                                                //본문에 키워드가 있거나
                                        .or(article.body.containsIgnoreCase(keyword)))

                )
                //날짜순 내림차순 정렬
                .orderBy(article.createDate.desc())
                //몇 개의 게시글을 가져올 지 설정
                .offset(pageable.getOffset())
                .limit(limit)
                //쿼리 실행, 결과 반환
                .fetch();

        boolean hasNext = fetched.size() > pageable.getPageSize();
        if(hasNext) {
            //추가로 가져온 마지막 항목 제거
            fetched.remove(fetched.size() - 1);
        }

        return new SliceImpl<>(fetched, pageable, hasNext);
    }

    @Override
    public Slice<Article> findAllBy(Long subCategoryId, Pageable pageable){
        QArticle article = QArticle.article;
        int size = pageable.getPageSize();

        List<Article> fetched = queryFactory
                .selectFrom(article)
                .where(article.subCategory.id.eq(article.subCategory.id))
                .orderBy(article.createDate.desc())
                .offset(pageable.getOffset())
                .limit(size + 1)
                .fetch();

        boolean hasNext = fetched.size() > size;
        if (hasNext) {
            fetched.remove(size);
        }
        return new SliceImpl<>(fetched, pageable, hasNext);
    }
}
