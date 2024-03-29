package reduck.reduck.domain.post.entity.mapper;

import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostType;

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
