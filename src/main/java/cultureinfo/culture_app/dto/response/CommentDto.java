package cultureinfo.culture_app.dto.response;

import cultureinfo.culture_app.domain.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private Long memberId;
    private String memberName;
    private LocalDateTime createdDate;
    private Long likeCount;

    public static CommentDto from(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getCommentContent())
                .memberId(comment.getMember().getId())
                .memberName(comment.getMember().getNickname())
                .createdDate(comment.getCreateDate())
                .likeCount(comment.getLikeCount())
                .build();
    }
}

