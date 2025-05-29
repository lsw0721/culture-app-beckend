package cultureinfo.culture_app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentSubCategory is a Querydsl query type for ContentSubCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentSubCategory extends EntityPathBase<ContentSubCategory> {

    private static final long serialVersionUID = 1840725311L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentSubCategory contentSubCategory = new QContentSubCategory("contentSubCategory");

    public final QContentCategory contentCategory;

    public final ListPath<ContentDetail, QContentDetail> details = this.<ContentDetail, QContentDetail>createList("details", ContentDetail.class, QContentDetail.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QContentSubCategory(String variable) {
        this(ContentSubCategory.class, forVariable(variable), INITS);
    }

    public QContentSubCategory(Path<? extends ContentSubCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentSubCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentSubCategory(PathMetadata metadata, PathInits inits) {
        this(ContentSubCategory.class, metadata, inits);
    }

    public QContentSubCategory(Class<? extends ContentSubCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contentCategory = inits.isInitialized("contentCategory") ? new QContentCategory(forProperty("contentCategory")) : null;
    }

}

