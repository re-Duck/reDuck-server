package reduck.reduck.domain.post.dto;

import lombok.*;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.tag.dto.TagDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String postOriginId;
    @NotNull
    private PostType postType;

    private List<TagDto> tags;
}
