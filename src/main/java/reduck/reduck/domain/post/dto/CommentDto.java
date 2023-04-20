package reduck.reduck.domain.post.dto;

import lombok.Getter;

@Getter
public class CommentDto {

    private String userId;
    private String content;
    private String commentOriginId;
    private String postOriginId;
}
