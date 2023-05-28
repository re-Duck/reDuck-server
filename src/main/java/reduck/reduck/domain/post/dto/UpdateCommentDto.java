package reduck.reduck.domain.post.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
@Getter
public class UpdateCommentDto {

    @NotBlank
    private String content;
    @NotBlank
    private String commentOriginId;
}
