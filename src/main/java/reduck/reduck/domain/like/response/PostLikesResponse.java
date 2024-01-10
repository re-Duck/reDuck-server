package reduck.reduck.domain.like.response;

import lombok.Builder;
import lombok.Getter;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.util.DevelopAnnualCalculation;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostLikesResponse {
    //user
    private String postAuthorId;
    private String postAuthorProfileImgPath;
    private String postAuthorName;
    private String postAuthorDevelopAnnual;

    //post
    private String postTitle;
    private String postContent;
    private String postOriginId;
    private PostType postType;
    private LocalDateTime postCreatedAt;
    private LocalDateTime postUpdatedAt;

    public static PostLikesResponse from(Post post) {
        String developAnnual = DevelopAnnualCalculation.calculate(post.getUser().getDevelopYear());

        return PostLikesResponse.builder()
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
                .postUpdatedAt(post.getUpdatedAt()).build();
    }
}
