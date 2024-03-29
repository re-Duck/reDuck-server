package reduck.reduck.domain.post.dto;

import jakarta.validation.constraints.Size;
import lombok.*;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.tag.dto.TagDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    private String thumbnailImagePath;

    @Size(max = 100)
    private String thumbnailContent;

    private List<TagDto> tags;
}
