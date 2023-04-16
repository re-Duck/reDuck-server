package reduck.reduck.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reduck.reduck.domain.post.dto.CommentDto;
import reduck.reduck.domain.post.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/post/comment/{postOriginId}/{commentOriginId}")
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto, @PathVariable String postOriginId, @PathVariable String commentOriginId) {
        commentService.createComment(commentDto, postOriginId, commentOriginId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
