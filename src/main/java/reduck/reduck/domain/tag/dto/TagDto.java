package reduck.reduck.domain.tag.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagDto {
    private String title;

    public static TagDto from(TagDto dto) {
        return TagDto.builder().title(dto.getTitle()).build();
    }
}
