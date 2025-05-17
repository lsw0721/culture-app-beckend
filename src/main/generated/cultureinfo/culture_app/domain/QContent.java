package cultureinfo.culture_app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContent is a Querydsl query type for Content
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContent extends EntityPathBase<Content> {

    private static final long serialVersionUID = -18038625L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContent content = new QContent("content");

    public final ListPath<ContentFavorite, QContentFavorite> contentFavorites = this.<ContentFavorite, QContentFavorite>createList("contentFavorites", ContentFavorite.class, QContentFavorite.class, PathInits.DIRECT2);

    public final StringPath contentName = createString("contentName");

    public final QContentSubcategory contentSubCategory;

    public final StringPath details = createString("details");

    public final DateTimePath<java.time.LocalDateTime> endDateTime = createDateTime("endDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath location = createString("location");

    public final StringPath picture = createString("picture");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startDateTime = createDateTime("startDateTime", java.time.LocalDateTime.class);

    public QContent(String variable) {
        this(Content.class, forVariable(variable), INITS);
    }

    public QContent(Path<? extends Content> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContent(PathMetadata metadata, PathInits inits) {
        this(Content.class, metadata, inits);
    }

    public QContent(Class<? extends Content> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contentSubCategory = inits.isInitialized("contentSubCategory") ? new QContentSubcategory(forProperty("contentSubCategory"), inits.get("contentSubCategory")) : null;
    }

}

