package reduck.reduck.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.post.dto.CommentDto;
import reduck.reduck.domain.post.dto.UpdateCommentDto;
import reduck.reduck.domain.post.service.CommentService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post/comment")
    public ResponseEntity<Void> createComment(@RequestBody @Valid CommentDto commentDto) {
        commentService.createComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/post/comment/{commentOriginId}")
    public ResponseEntity<Void> createComment(@PathVariable String commentOriginId) {
        commentService.removeComment(commentOriginId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/post/comment/{commentOriginId}")
    public ResponseEntity<Void> updateComment(@PathVariable String commentOriginId, @RequestBody @Valid UpdateCommentDto commentDto) {
        commentService.updateComment(commentOriginId, commentDto);
        return ResponseEntity.ok().build();
    }
}
