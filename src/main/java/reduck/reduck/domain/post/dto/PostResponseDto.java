package reduck.reduck.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.entity.UserProfileImg;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PostResponseDto {
    //user
    private String postAuthorId;
    private UserProfileImg postAuthorProfileimg;
    private String postAuthorName;

    //post
    private String postTitle;
    private String postContent;
    private String postOriginId;
    private PostType postType;
    private LocalDateTime postCreatedAt;
    private LocalDateTime postUpdatedAt;

    //comment
    private List<CommentResponseDto> comments;
}
