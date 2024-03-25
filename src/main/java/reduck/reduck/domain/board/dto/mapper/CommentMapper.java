package reduck.reduck.domain.board.dto.mapper;

import reduck.reduck.domain.board.dto.CommentDto;
import reduck.reduck.domain.board.entity.Comment;
import reduck.reduck.domain.board.entity.Post;
import reduck.reduck.domain.user.entity.User;

public class CommentMapper {

    public static Comment replyOf(CommentDto dto, Post post, User user) {
        return Comment.builder()
                .commentContent(dto.getContent())
                .post(post)
                .commentOriginId(dto.getCommentOriginId())
                .user(user)
                .parentCommentOriginId(dto.getParentCommentOriginId())
                .likes(0)
                .build();
    }
}
