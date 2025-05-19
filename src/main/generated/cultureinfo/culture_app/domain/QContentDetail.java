package cultureinfo.culture_app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentDetail is a Querydsl query type for ContentDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentDetail extends EntityPathBase<ContentDetail> {

    private static final long serialVersionUID = 522883536L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentDetail contentDetail = new QContentDetail("contentDetail");

    public final ListPath<ContentFavorite, QContentFavorite> contentFavorite = this.<ContentFavorite, QContentFavorite>createList("contentFavorite", ContentFavorite.class, QContentFavorite.class, PathInits.DIRECT2);

    public final StringPath contentName = createString("contentName");

    public final QContentSmallCategory contentSmallCategory;

    public final StringPath details = createString("details");

    public final DateTimePath<java.time.LocalDateTime> endDateTime = createDateTime("endDateTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> favoriteCount = createNumber("favoriteCount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath location = createString("location");

    public final StringPath picture = createString("picture");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startDateTime = createDateTime("startDateTime", java.time.LocalDateTime.class);

    public QContentDetail(String variable) {
        this(ContentDetail.class, forVariable(variable), INITS);
    }

    public QContentDetail(Path<? extends ContentDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentDetail(PathMetadata metadata, PathInits inits) {
        this(ContentDetail.class, metadata, inits);
    }

    public QContentDetail(Class<? extends ContentDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contentSmallCategory = inits.isInitialized("contentSmallCategory") ? new QContentSmallCategory(forProperty("contentSmallCategory"), inits.get("contentSmallCategory")) : null;
    }

}

