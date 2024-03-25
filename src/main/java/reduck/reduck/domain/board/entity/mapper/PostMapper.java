package reduck.reduck.domain.board.entity.mapper;

import reduck.reduck.domain.board.dto.PostDto;
import reduck.reduck.domain.board.entity.Post;

public class PostMapper {
    public static Post from(PostDto obj) {
        Post post = Post.builder()
                .postOriginId(obj.getPostOriginId())
                .postTitle(obj.getTitle())
                .postType(obj.getPostType())
                .content(obj.getContent())
                .thumbnailContent(obj.getThumbnailContent())
                .thumbnailImagePath(obj.getThumbnailImagePath())
                .build();
        return post;

    }
}
