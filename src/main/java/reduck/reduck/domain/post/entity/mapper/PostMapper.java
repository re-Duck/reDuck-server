package reduck.reduck.domain.post.entity.mapper;

import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.entity.Post;

public class PostMapper {
    public static Post of(PostDto obj, String path) {
        Post post = Post.builder()
                .postOriginId(obj.getPostOriginId())
                .postTitle(obj.getTitle())
                .postType(obj.getPostType())
                .contentPath(path)
                .build();
        return post;

    }
}
