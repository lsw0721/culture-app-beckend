package cultureinfo.culture_app.dto.response;

import cultureinfo.culture_app.domain.Article;
import cultureinfo.culture_app.domain.type.ArticleCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

// 게시글 상세 페이지 dto
public class ArticleDto {
    private Long id;
    private String title;
    private String body;
    private ArticleCategory category;
    private LocalDateTime createDate;
    private Long likeCount;
    private Long commentCount;
    private String writerName;

    public static ArticleDto from(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .body(article.getBody())
                .category(article.getCategory())
                .createDate(article.getCreateDate())
                .likeCount(article.getLikeCount())
                .commentCount(article.getCommentCount())
                .writerName(article.getMember().getNickname())
                .build();
    }
}
