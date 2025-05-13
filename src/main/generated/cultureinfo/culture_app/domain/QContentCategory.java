package cultureinfo.culture_app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentCategory is a Querydsl query type for ContentCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContentCategory extends EntityPathBase<ContentCategory> {

    private static final long serialVersionUID = -1014312259L;

    public static final QContentCategory contentCategory = new QContentCategory("contentCategory");

    public final StringPath contentCategoryName = createString("contentCategoryName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QContentCategory(String variable) {
        super(ContentCategory.class, forVariable(variable));
    }

    public QContentCategory(Path<? extends ContentCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentCategory(PathMetadata metadata) {
        super(ContentCategory.class, metadata);
    }

}

