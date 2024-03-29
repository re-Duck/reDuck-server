package reduck.reduck.domain.post.dto.mapper;

import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.tag.dto.TagDto;
import reduck.reduck.domain.tag.entity.Tag;
import reduck.reduck.util.DevelopAnnualCalculation;

import java.util.*;
import java.util.stream.Collectors;

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
                .thumbnailContent(post.getThumbnailContent())
                .thumbnailImagePath(post.getThumbnailImagePath())
                .postCreatedAt(post.getCreatedAt())
                .postUpdatedAt(post.getUpdatedAt())

                .commentsCount(post.getComments().size())
                .hits(post.getPostHit().getHits())
                .build();
        return postResponseDto;
    }

    public static PostResponseDto of(Post post, int likes, List<Tag> tags) {
        PostResponseDto from = from(post);
        from.setLikes(likes);
        List<TagDto> tagDtos = tags.stream().map(tag -> TagDto.from(tag)).collect(Collectors.toList());
        from.setTags(tagDtos);
        return from;
    }

}
