package reduck.reduck.domain.board.dto;

import lombok.*;
import reduck.reduck.domain.board.entity.PostType;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostOfUserResponseDto {

    //post
    private String postTitle;
    private String postContent;
    private String postOriginId;
    private PostType postType;
    private LocalDateTime postCreatedAt;
    private LocalDateTime postUpdatedAt;


}
