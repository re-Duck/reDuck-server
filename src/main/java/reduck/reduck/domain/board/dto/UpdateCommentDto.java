package reduck.reduck.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
@Getter
@Setter
public class UpdateCommentDto {

    @NotBlank
    private String content;
    @NotBlank
    private String commentOriginId;
}
