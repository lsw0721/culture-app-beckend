package cultureinfo.culture_app.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 137879988L;

    public static final QMember member = new QMember("member1");

    public final ListPath<ArticleLike, QArticleLike> articleLikes = this.<ArticleLike, QArticleLike>createList("articleLikes", ArticleLike.class, QArticleLike.class, PathInits.DIRECT2);

    public final ListPath<CommentLike, QCommentLike> commentLikes = this.<CommentLike, QCommentLike>createList("commentLikes", CommentLike.class, QCommentLike.class, PathInits.DIRECT2);

    public final ListPath<ContentFavorite, QContentFavorite> contentFavorites = this.<ContentFavorite, QContentFavorite>createList("contentFavorites", ContentFavorite.class, QContentFavorite.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final EnumPath<cultureinfo.culture_app.domain.type.Gender> gender = createEnum("gender", cultureinfo.culture_app.domain.type.Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<cultureinfo.culture_app.domain.type.Keyword1> keyword1 = createEnum("keyword1", cultureinfo.culture_app.domain.type.Keyword1.class);

    public final EnumPath<cultureinfo.culture_app.domain.type.Keyword2> keyword2 = createEnum("keyword2", cultureinfo.culture_app.domain.type.Keyword2.class);

    public final EnumPath<cultureinfo.culture_app.domain.type.Keyword3> keyword3 = createEnum("keyword3", cultureinfo.culture_app.domain.type.Keyword3.class);

    public final StringPath location = createString("location");

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final SetPath<cultureinfo.culture_app.domain.type.Role, EnumPath<cultureinfo.culture_app.domain.type.Role>> roles = this.<cultureinfo.culture_app.domain.type.Role, EnumPath<cultureinfo.culture_app.domain.type.Role>>createSet("roles", cultureinfo.culture_app.domain.type.Role.class, EnumPath.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

