package reduck.reduck.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.tag.dto.TagDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    //user
    @Schema(description = "저자 Id", defaultValue = "reduck")
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

    //comment
    private int commentsCount;

    private int hits;
    private int likes;

    private List<TagDto> tags;

}
