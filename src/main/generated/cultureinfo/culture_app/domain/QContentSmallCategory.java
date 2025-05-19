package cultureinfo.culture_app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentSmallCategory is a Querydsl query type for ContentSmallCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentSmallCategory extends EntityPathBase<ContentSmallCategory> {

    private static final long serialVersionUID = -385259322L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentSmallCategory contentSmallCategory = new QContentSmallCategory("contentSmallCategory");

    public final StringPath ContentSmallCategoryName = createString("ContentSmallCategoryName");

    public final QContentSubcategory contentSubcategory;

    public final ListPath<ContentDetail, QContentDetail> details = this.<ContentDetail, QContentDetail>createList("details", ContentDetail.class, QContentDetail.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QContentSmallCategory(String variable) {
        this(ContentSmallCategory.class, forVariable(variable), INITS);
    }

    public QContentSmallCategory(Path<? extends ContentSmallCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentSmallCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentSmallCategory(PathMetadata metadata, PathInits inits) {
        this(ContentSmallCategory.class, metadata, inits);
    }

    public QContentSmallCategory(Class<? extends ContentSmallCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contentSubcategory = inits.isInitialized("contentSubcategory") ? new QContentSubcategory(forProperty("contentSubcategory"), inits.get("contentSubcategory")) : null;
    }

}

