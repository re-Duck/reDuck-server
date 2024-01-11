package reduck.reduck.domain.post.dto.mapper;

import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.dto.PostDetailResponseDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.util.DevelopAnnualCalculation;

import java.util.List;

public class PostDetailResponseDtoMapper {
    public static PostDetailResponseDto from(Post post) {
        String developAnnual = DevelopAnnualCalculation.calculate(post.getUser().getDevelopYear());
        List<CommentResponseDto> comments = CommentResponseDtoMapper.from(post);

        PostDetailResponseDto postDetailResponseDto = PostDetailResponseDto.builder()
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
                //comment
                .comments(comments)
                .hits(post.getPostHit().getHits())
                .likes(-1)
                .build();

        return postDetailResponseDto;
    }
}
