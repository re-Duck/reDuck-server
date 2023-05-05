package reduck.reduck.domain.post.dto;

import lombok.*;
import reduck.reduck.domain.post.entity.PostType;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostOfUserResponseDto {

    //post
    private String postTitle;
    private String postContentPath;
    private String postOriginId;
    private PostType postType;
    private LocalDateTime postCreatedAt;
    private LocalDateTime postUpdatedAt;


}
