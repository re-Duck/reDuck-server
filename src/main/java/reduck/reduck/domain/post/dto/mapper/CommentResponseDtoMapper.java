package reduck.reduck.domain.post.dto.mapper;
import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.util.DevelopAnnualCalculation;

public class CommentResponseDtoMapper {
    public static CommentResponseDto of(User user, Comment comment) {
        String developAnnual = DevelopAnnualCalculation.calculate(user.getDevelopYear());
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                //user
                .commentAuthorId(user.getUserId())
                .commentAuthorName(user.getName())
                .commentAuthorDevelopAnnual(developAnnual)
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
