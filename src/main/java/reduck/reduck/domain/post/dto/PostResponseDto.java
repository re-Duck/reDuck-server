package reduck.reduck.domain.post.dto;

import lombok.*;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.entity.UserProfileImg;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    //user
    private String postAuthorId;
    private UserProfileImg postAuthorProfileImg;
    private String postAuthorName;
    private String postAuthorDevelopAnnual;

    //post
    private String postTitle;
    private String postContentPath;
    private String postOriginId;
    private PostType postType;
    private LocalDateTime postCreatedAt;
    private LocalDateTime postUpdatedAt;

    //comment
    private List<CommentResponseDto> comments;

    @Override
    public String toString() {
        return "PostResponseDto{" +
                "postAuthorId='" + postAuthorId + '\'' +
                ", postAuthorProfileImg=" + postAuthorProfileImg +
                ", postAuthorName='" + postAuthorName + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", postContent='" + postContentPath + '\'' +
                ", postOriginId='" + postOriginId + '\'' +
                ", postType=" + postType +
                ", postCreatedAt=" + postCreatedAt +
                ", postUpdatedAt=" + postUpdatedAt +
                 +
                '}';
    }
}
