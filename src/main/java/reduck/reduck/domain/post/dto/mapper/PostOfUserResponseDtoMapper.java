package reduck.reduck.domain.post.dto.mapper;

import reduck.reduck.domain.post.dto.PostOfUserResponseDto;
import reduck.reduck.domain.post.entity.Post;

import java.util.ArrayList;
import java.util.List;

public class PostOfUserResponseDtoMapper {

    public static List<PostOfUserResponseDto> from(List<Post> posts) {
        List<PostOfUserResponseDto> dtos = new ArrayList<>();
        for (Post post : posts) {
            PostOfUserResponseDto build = PostOfUserResponseDto.builder()
                    .postTitle(post.getPostTitle())
                    .postContentPath(post.getContentPath())
                    .postOriginId(post.getPostOriginId())
                    .postType(post.getPostType())
                    .postCreatedAt(post.getCreatedAt())
                    .postUpdatedAt(post.getUpdatedAt())
                    .build();
            dtos.add(build);

        }
        return dtos;
    }
}
