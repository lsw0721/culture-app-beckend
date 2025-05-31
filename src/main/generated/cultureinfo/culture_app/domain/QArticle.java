package cultureinfo.culture_app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QArticle is a Querydsl query type for Article
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QArticle extends EntityPathBase<Article> {

    private static final long serialVersionUID = -1701947108L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QArticle article = new QArticle("article");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final ListPath<ArticleLike, QArticleLike> articleLikes = this.<ArticleLike, QArticleLike>createList("articleLikes", ArticleLike.class, QArticleLike.class, PathInits.DIRECT2);

    public final StringPath body = createString("body");

    public final EnumPath<cultureinfo.culture_app.domain.type.ArticleCategory> category = createEnum("category", cultureinfo.culture_app.domain.type.ArticleCategory.class);

    public final NumberPath<Long> commentCount = createNumber("commentCount", Long.class);

    //inherited
    public final StringPath createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final QMember member;

    public final QContentSubCategory subCategory;

    public final StringPath title = createString("title");

    public QArticle(String variable) {
        this(Article.class, forVariable(variable), INITS);
    }

    public QArticle(Path<? extends Article> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QArticle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QArticle(PathMetadata metadata, PathInits inits) {
        this(Article.class, metadata, inits);
    }

    public QArticle(Class<? extends Article> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.subCategory = inits.isInitialized("subCategory") ? new QContentSubCategory(forProperty("subCategory"), inits.get("subCategory")) : null;
    }

}

