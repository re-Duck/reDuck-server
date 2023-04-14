package reduck.reduck.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import reduck.reduck.domain.user.entity.UserProfileImg;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {

    private String commentAuthor;
    private String commentAuthorName;
    private UserProfileImg commentAuthorProfileImg;

    private String commentContent;
    private String commentOriginId;
    private LocalDateTime updatedAt;

}
