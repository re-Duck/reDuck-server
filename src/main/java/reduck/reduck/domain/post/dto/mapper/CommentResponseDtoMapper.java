package reduck.reduck.domain.post.dto.mapper;
import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.util.DevelopAnnualCalculation;

import java.util.ArrayList;
import java.util.List;

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

    public static List<CommentResponseDto> from(Post post) {
        List<CommentResponseDto> comments = new ArrayList<>();

        for (Comment comm : post.getComments()) {
            CommentResponseDto commentResponseDto = CommentResponseDtoMapper.of(comm.getUser(), comm);
            comments.add(commentResponseDto);
        }
        return comments;
    }
}
