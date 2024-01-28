package reduck.reduck.domain.tag.mapper;

import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.tag.dto.TagDto;
import reduck.reduck.domain.tag.entity.Tag;

public class TagMapper {
    public static Tag of(Post post, TagDto dto) {
        return Tag.builder()
                .post(post)
                .title(dto.getTitle())
                .build();
    }
}
