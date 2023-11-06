package reduck.reduck.domain.post.dto;

import lombok.*;
import reduck.reduck.domain.post.entity.PostType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailResponseDto {
    //user
    private String postAuthorId;
    private String postAuthorProfileImgPath;
    private String postAuthorName;
    private String postAuthorDevelopAnnual;

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
