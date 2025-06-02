package cultureinfo.culture_app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentSession is a Querydsl query type for ContentSession
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentSession extends EntityPathBase<ContentSession> {

    private static final long serialVersionUID = -543213321L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentSession contentSession = new QContentSession("contentSession");

    public final ListPath<String, StringPath> artistNames = this.<String, StringPath>createList("artistNames", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> booths = this.<String, StringPath>createList("booths", String.class, StringPath.class, PathInits.DIRECT2);

    public final QContentDetail contentDetail;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath sessionDate = createString("sessionDate");

    public QContentSession(String variable) {
        this(ContentSession.class, forVariable(variable), INITS);
    }

    public QContentSession(Path<? extends ContentSession> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentSession(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentSession(PathMetadata metadata, PathInits inits) {
        this(ContentSession.class, metadata, inits);
    }

    public QContentSession(Class<? extends ContentSession> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contentDetail = inits.isInitialized("contentDetail") ? new QContentDetail(forProperty("contentDetail"), inits.get("contentDetail")) : null;
    }

}

