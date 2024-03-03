package reduck.reduck.domain.post.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentDto {

    @NotBlank
    private String content;
    @NotBlank
    private String commentOriginId;
    @NotBlank
    private String postOriginId;

    private String parentCommentOriginId;
}
