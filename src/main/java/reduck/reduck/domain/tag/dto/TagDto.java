package reduck.reduck.domain.tag.dto;

import lombok.Builder;
import lombok.Getter;
import reduck.reduck.domain.tag.entity.Tag;
import reduck.reduck.domain.tag.entity.TemporaryTag;

@Getter
@Builder
public class TagDto {
    private String title;
    public static TagDto from(Tag tag) {
        return TagDto.builder()
                .title(tag.getTitle())
                .build();
    }
    public static TagDto from(TemporaryTag tag) {
        return TagDto.builder()
                .title(tag.getTitle())
                .build();
    }
}
