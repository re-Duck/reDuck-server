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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostService postService;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public void createComment(CommentDto commentDto, String postOriginId, String commentOriginId) {
        User byUserId = userService.findByUserId(commentDto.getUserId());
        Optional<Post> byPostOriginId = postRepository.findByPostOriginId(postOriginId);
//        Post byPostOriginId = postService.findByPostOriginId(postOriginId);
        Comment build = Comment.builder()
                .commentContent(commentDto.getContent())
                .post(byPostOriginId.get())
                .commentOriginId(commentOriginId)
                .user(byUserId)
                .build();
        System.out.println("build = " + build.getUser());
        System.out.println("build = " + build.getPost());
        Comment save = commentRepository.save(build);
        System.out.println("save.toString() = " + save.toString());

    }
}
