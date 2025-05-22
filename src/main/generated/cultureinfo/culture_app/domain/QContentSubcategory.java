package cultureinfo.culture_app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContentSubcategory is a Querydsl query type for ContentSubcategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentSubcategory extends EntityPathBase<ContentSubcategory> {

    private static final long serialVersionUID = 1776081183L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContentSubcategory contentSubcategory = new QContentSubcategory("contentSubcategory");

    public final QContentCategory contentCategory;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<ContentSmallCategory, QContentSmallCategory> smallCategories = this.<ContentSmallCategory, QContentSmallCategory>createList("smallCategories", ContentSmallCategory.class, QContentSmallCategory.class, PathInits.DIRECT2);

    public QContentSubcategory(String variable) {
        this(ContentSubcategory.class, forVariable(variable), INITS);
    }

    public QContentSubcategory(Path<? extends ContentSubcategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContentSubcategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContentSubcategory(PathMetadata metadata, PathInits inits) {
        this(ContentSubcategory.class, metadata, inits);
    }

    public QContentSubcategory(Class<? extends ContentSubcategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contentCategory = inits.isInitialized("contentCategory") ? new QContentCategory(forProperty("contentCategory")) : null;
    }

}

