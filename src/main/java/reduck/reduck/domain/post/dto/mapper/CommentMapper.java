package reduck.reduck.domain.post.dto.mapper;

import reduck.reduck.domain.post.dto.CommentDto;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;
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
