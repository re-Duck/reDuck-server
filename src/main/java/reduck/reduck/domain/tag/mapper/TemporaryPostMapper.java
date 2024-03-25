package reduck.reduck.domain.tag.mapper;

import reduck.reduck.domain.board.dto.PostDto;
import reduck.reduck.domain.board.entity.TemporaryPost;
import reduck.reduck.domain.user.entity.User;

public class TemporaryPostMapper {
    static public TemporaryPost of(PostDto dto, User user) {
        return TemporaryPost.builder()
                .postType(dto.getPostType())
                .postTitle(dto.getTitle())
                .postOriginId(dto.getPostOriginId())
                .content(dto.getContent())
                .user(user)
                .build();
    }
}
