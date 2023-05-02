package reduck.reduck.domain.post.dto.mapper;

import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.util.DevelopAnnualCalculation;

import java.util.*;

public class PostResponseDtoMapper {

    public static PostResponseDto excludeCommentsFrom(Post post) {
        String developAnnual = DevelopAnnualCalculation.calculate(post.getUser().getDevelopYear());

        PostResponseDto postResponseDto = PostResponseDto.builder()
                //user
                .postAuthorId(post.getUser().getUserId())
                .postAuthorName(post.getUser().getName())
                .postAuthorProfileImg(post.getUser().getProfileImg())
                .postAuthorDevelopAnnual(developAnnual)
                //post
                .postTitle(post.getPostTitle())
                .postContentPath(post.getContentPath())
                .postOriginId(post.getPostOriginId())
                .postType(post.getPostType())
                .postCreatedAt(post.getCreatedAt())
                .postUpdatedAt(post.getUpdatedAt())
                .build();
        return postResponseDto;
    }

    public static PostResponseDto from(Post post) {
        List<CommentResponseDto> comments = new ArrayList<>();
        String developAnnual = DevelopAnnualCalculation.calculate(post.getUser().getDevelopYear());

        for (Comment comm : post.getComments()) {
            CommentResponseDto commentResponseDto = CommentResponseDtoMapper.of(comm.getUser(), comm);
            comments.add(commentResponseDto);
        }
        PostResponseDto postResponseDto = PostResponseDto.builder()
                //user
                .postAuthorId(post.getUser().getUserId())
                .postAuthorName(post.getUser().getName())
                .postAuthorProfileImg(post.getUser().getProfileImg())
                .postAuthorDevelopAnnual(developAnnual)
                //post
                .postTitle(post.getPostTitle())
                .postContentPath(post.getContentPath())
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
