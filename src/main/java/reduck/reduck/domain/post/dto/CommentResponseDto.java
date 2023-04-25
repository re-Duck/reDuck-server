package reduck.reduck.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import reduck.reduck.domain.user.entity.UserProfileImg;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    //user
    private String commentAuthorId;
    private String commentAuthorName;
    private UserProfileImg commentAuthorProfileImg;
    private String commentAuthorDevelopAnnual;

    //comment
    private String commentContent;
    private String commentOriginId;
    private LocalDateTime commentUpdatedAt;
    private LocalDateTime commentCreatedAt;

}
