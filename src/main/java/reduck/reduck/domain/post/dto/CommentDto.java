package reduck.reduck.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentDto {

    @NotBlank
    private String content;
    @NotBlank
    private String commentOriginId;
    @NotBlank
    private String postOriginId;
}
