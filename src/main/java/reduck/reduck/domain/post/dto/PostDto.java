package reduck.reduck.domain.post.dto;

import lombok.*;
import reduck.reduck.domain.post.entity.PostType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    @NotBlank
    private String title;

    @NotBlank
    private String postOriginId;

    private PostType postType;
}
