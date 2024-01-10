package reduck.reduck.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.post.dto.CommentDto;
import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.dto.UpdateCommentDto;
import reduck.reduck.domain.post.dto.mapper.CommentResponseDtoMapper;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.repository.CommentRepository;
import reduck.reduck.domain.post.repository.PostRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.service.UserService;
import reduck.reduck.global.exception.errorcode.AuthErrorCode;
import reduck.reduck.global.exception.errorcode.CommentErrorCode;
import reduck.reduck.global.exception.errorcode.PostErrorCode;
import reduck.reduck.global.exception.exception.*;
import reduck.reduck.util.AuthenticationToken;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
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

    @Transactional
    public void removeComment(String commentOriginId) {
        Comment comment = commentRepository.findByCommentOriginId(commentOriginId).orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_EXIST));
        validateAuthentication(comment);
        commentRepository.delete(comment);
    }

    public void updateComment(String commentOriginId, UpdateCommentDto commentDto) {
        Comment comment = commentRepository.findByCommentOriginId(commentOriginId).orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_EXIST));
        validateAuthentication(comment);
        comment.updateFrom(commentDto);
        commentRepository.save(comment);
    }

    private void validateAuthentication(Comment comment) {
        String userId = AuthenticationToken.getUserId();
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new AuthException(AuthErrorCode.NOT_AUTHORIZED);
        }
    }

    /**
     * 게시글의 댓글 조회
     *
     * @param postOriginId
     * @return
     */
    public List<CommentResponseDto> getComments(String postOriginId) {
        Post post = postRepository.findByPostOriginId(postOriginId)
                .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        List<Comment> comments = commentRepository.findAllByPost(post);
        return comments.stream()
                .map(comment -> CommentResponseDtoMapper.of(comment.getUser(), comment))
                .collect(Collectors.toList());
    }
}
