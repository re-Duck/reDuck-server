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
                .postAuthorProfileImgPath(post.getUser().getProfileImg().getPath())
                .postAuthorDevelopAnnual(developAnnual)
                //post
                .postTitle(post.getPostTitle())
                .postContent(post.getContent())
                .postOriginId(post.getPostOriginId())
                .postType(post.getPostType())
                .postCreatedAt(post.getCreatedAt())
                .postUpdatedAt(post.getUpdatedAt())
                .build();
        return postResponseDto;
    }

    public static PostResponseDto from(Post post) {
        String developAnnual = DevelopAnnualCalculation.calculate(post.getUser().getDevelopYear());
        List<CommentResponseDto> comments = CommentResponseDtoMapper.from(post);

        PostResponseDto postResponseDto = PostResponseDto.builder()
                //user
                .postAuthorId(post.getUser().getUserId())
                .postAuthorName(post.getUser().getName())
                .postAuthorProfileImgPath(post.getUser().getProfileImg().getPath())
                .postAuthorDevelopAnnual(developAnnual)
                //post
                .postTitle(post.getPostTitle())
                .postContent(post.getContent())
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
