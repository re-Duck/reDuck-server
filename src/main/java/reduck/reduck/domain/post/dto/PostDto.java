package reduck.reduck.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import reduck.reduck.domain.post.entity.PostType;

@Getter
@Setter
public class PostDto {
    private String title;
    private String userId;
    private String content;
    private String postOriginId;
    private PostType postType;
}
