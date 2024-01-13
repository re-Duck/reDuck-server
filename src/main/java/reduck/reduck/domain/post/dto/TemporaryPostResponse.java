package reduck.reduck.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.post.entity.TemporaryPost;

import java.time.LocalDateTime;

@Getter
@Builder
public class TemporaryPostResponse {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String postTitle;
    private String content;
    private String postOriginId;
    private PostType postType;

    private String userId;
    private String userName;
    private String userProfileImg;

    public static TemporaryPostResponse from(TemporaryPost temporaryPost) {
        return TemporaryPostResponse.builder()
                .createdAt(temporaryPost.getCreatedAt())
                .updatedAt(temporaryPost.getUpdatedAt())
                .postTitle(temporaryPost.getPostTitle())
                .content(temporaryPost.getContent())
                .postOriginId(temporaryPost.getPostOriginId())
                .postType(temporaryPost.getPostType())

                .userId(temporaryPost.getUser().getUserId())
                .userName(temporaryPost.getUser().getName())
                .userProfileImg(temporaryPost.getUser().getProfileImgPath())
                .build();
    }
}
