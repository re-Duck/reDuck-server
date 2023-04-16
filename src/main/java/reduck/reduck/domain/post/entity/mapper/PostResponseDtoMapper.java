package reduck.reduck.domain.post.entity.mapper;

import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;
import java.util.ArrayList;
import java.util.List;

public class PostResponseDtoMapper {

    public static PostResponseDto from(Post post){
        List<CommentResponseDto> comments = new ArrayList<>();
        for (Comment comm : post.getComments()) {
            CommentResponseDto commentResponseDto = CommentResponseDtoMapper.of(post.getUser(), comm);
            comments.add(commentResponseDto);
        }
        PostResponseDto postResponseDto = PostResponseDto.builder()
                //user
                .postAuthorId(post.getUser().getUserId())
                .postAuthorName(post.getUser().getName())
                .postAuthorProfileImg(post.getUser().getProfileImg())
                //post
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .postOriginId(post.getPostOriginId())
                .postType(post.getPostType())
                .postCreatedAt(post.getCreatedAt())
                .postUpdatedAt(post.getUpdatedAt())
                //comment
                .comments(comments)
                .build();
        return postResponseDto;
    }
}
