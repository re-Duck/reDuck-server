package reduck.reduck.domain.board.dto;

import lombok.*;
import reduck.reduck.domain.board.entity.PostType;

import java.time.LocalDateTime;

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
    private String thumbnailContent;
    private String thumbnailImagePath;
    private LocalDateTime postCreatedAt;
    private LocalDateTime postUpdatedAt;

    private int hits;
    private int likes;
}
