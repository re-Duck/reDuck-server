package reduck.reduck.domain.post.dto;

import lombok.*;
import reduck.reduck.domain.post.entity.PostType;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    @NotBlank
    private String title;
    @NotBlank
    private String userId;
    @NotBlank
    private String postOriginId;
    @NotBlank
    private PostType postType;
}
