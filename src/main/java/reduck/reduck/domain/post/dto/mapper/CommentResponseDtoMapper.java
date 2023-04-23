package reduck.reduck.domain.post.dto.mapper;
import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.user.entity.User;

public class CommentResponseDtoMapper {
    public static CommentResponseDto of(User user, Comment comment) {
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                //user
                .commentAuthorId(user.getUserId())
                .commentAuthorName(user.getName())
                .commentAuthorProfileImg(user.getProfileImg())
                //comment
                .commentCreatedAt(comment.getCreatedAt())
                .commentUpdatedAt(comment.getUpdatedAt())
                .commentContent(comment.getCommentContent())
                .commentOriginId(comment.getCommentOriginId())
                .build();
        return commentResponseDto;
    }
}
