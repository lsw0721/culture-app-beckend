package cultureinfo.culture_app.dto.response;

import cultureinfo.culture_app.domain.Article;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder

//게시글 목록 전용 dto
public class ArticleSummaryDto {
    private Long id;
    private String title;
    private String previewBody;
    private Long CommentCount;
    private LocalDateTime createDate;

    public static ArticleSummaryDto from(Article article) {
        return ArticleSummaryDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                //목록에서는 본문의 내용 20자 이하로 출력
                .previewBody(article.getBody().length() > 20 ? article.getBody().substring(0, 20) + "..." : article.getBody())
                .CommentCount(article.getCommentCount())
                .createDate(article.getCreateDate())
                .build();
    }
}
