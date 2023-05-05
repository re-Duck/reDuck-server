package reduck.reduck.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.post.dto.CommentDto;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.repository.CommentRepository;
import reduck.reduck.domain.post.repository.PostRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.service.UserService;
import reduck.reduck.global.exception.errorcode.PostErrorCode;
import reduck.reduck.global.exception.exception.PostException;
import reduck.reduck.util.AuthenticationToken;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public void createComment(CommentDto commentDto) {
        String postOriginId = commentDto.getPostOriginId();
        String commentOriginId = commentDto.getCommentOriginId();
        User user = userService.findByUserId(AuthenticationToken.getUserId());
        Post post = postRepository.findByPostOriginId(postOriginId).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));
        Comment comment = Comment.builder()
                .commentContent(commentDto.getContent())
                .post(post)
                .commentOriginId(commentOriginId)
                .user(user)
                .build();
        commentRepository.save(comment);
    }
}
