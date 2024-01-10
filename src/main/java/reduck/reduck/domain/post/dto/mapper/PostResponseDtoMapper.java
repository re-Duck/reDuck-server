package reduck.reduck.domain.post.dto.mapper;

import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.util.DevelopAnnualCalculation;

import java.util.*;

public class PostResponseDtoMapper {

    public static PostResponseDto from(Post post) {
        String developAnnual = DevelopAnnualCalculation.calculate(post.getUser().getDevelopYear());

        PostResponseDto postResponseDto = PostResponseDto.builder()
                //user
                .postAuthorId(post.getUser().getUserId())
                .postAuthorName(post.getUser().getName())
                .postAuthorProfileImgPath(post.getUser().getProfileImgPath())
                .postAuthorDevelopAnnual(developAnnual)
                //post
                .postTitle(post.getPostTitle())
                .postContent(post.getContent())
                .postOriginId(post.getPostOriginId())
                .postType(post.getPostType())
                .postCreatedAt(post.getCreatedAt())
                .postUpdatedAt(post.getUpdatedAt())

                .commentsCount(post.getComments().size())
                .hits(post.getPostHit().getHits())
                .likes(-1)
                .build();
        return postResponseDto;
    }

    public static PostResponseDto of(Post post, int likes) {
        PostResponseDto from = from(post);
        from.setLikes(likes);
        return from;
    }

}
