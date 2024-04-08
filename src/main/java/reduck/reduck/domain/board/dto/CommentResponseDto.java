package reduck.reduck.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    //user
    private String commentAuthorId;
    private String commentAuthorName;
    private String commentAuthorProfileImgPath;
    private String commentAuthorDevelopAnnual;

    //comment
    private String commentContent;
    private String parentCommentOriginId;
    private String commentOriginId;
    private LocalDateTime commentUpdatedAt;
    private LocalDateTime commentCreatedAt;
}
