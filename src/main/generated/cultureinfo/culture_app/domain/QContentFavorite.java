package cultureinfo.culture_app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentFavorite is a Querydsl query type for ContentFavorite
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentFavorite extends EntityPathBase<ContentFavorite> {

    private static final long serialVersionUID = -14033061L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentFavorite contentFavorite = new QContentFavorite("contentFavorite");

    public final QContentDetail contentDetail;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public QContentFavorite(String variable) {
        this(ContentFavorite.class, forVariable(variable), INITS);
    }

    public QContentFavorite(Path<? extends ContentFavorite> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentFavorite(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentFavorite(PathMetadata metadata, PathInits inits) {
        this(ContentFavorite.class, metadata, inits);
    }

    public QContentFavorite(Class<? extends ContentFavorite> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contentDetail = inits.isInitialized("contentDetail") ? new QContentDetail(forProperty("contentDetail"), inits.get("contentDetail")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

