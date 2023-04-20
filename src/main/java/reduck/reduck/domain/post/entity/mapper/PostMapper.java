package reduck.reduck.domain.post.entity.mapper;

import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.entity.Post;

public class PostMapper {
    public static Post from(PostDto obj) {
        Post post = Post.builder()
                .postOriginId(obj.getPostOriginId())
                .postContent(obj.getContent())
                .postTitle(obj.getTitle())
                .postType(obj.getPostType())
                .build();
        return post;

    }
}
