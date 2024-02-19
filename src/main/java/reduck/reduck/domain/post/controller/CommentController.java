package reduck.reduck.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.post.dto.CommentDto;
import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.dto.UpdateCommentDto;
import reduck.reduck.domain.post.service.CommentService;
import reduck.reduck.global.entity.Response;
import reduck.reduck.global.security.CustomUserDetails;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/post/comment")
    public ResponseEntity<Void> createComment(@RequestBody @Valid CommentDto commentDto) {
        commentService.createComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comments/posts/reply")
    public ResponseEntity<Void> createReplyComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid CommentDto commentDto) {
        commentService.createReplyComment(customUserDetails.getUser(), commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/post/comment/{commentOriginId}")
    public ResponseEntity<Void> removeComment(@PathVariable String commentOriginId) {
        commentService.removeComment(commentOriginId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/post/comment/{commentOriginId}")
    public ResponseEntity<Void> updateComment(@PathVariable String commentOriginId, @RequestBody @Valid UpdateCommentDto commentDto) {
        commentService.updateComment(commentOriginId, commentDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post/{postOriginId}/comments")
    public ResponseEntity<Response<List<CommentResponseDto>>> getComments(
            @PathVariable("postOriginId") String postOriginId
    ) {
        List<CommentResponseDto> commentResponseDtos = commentService.getComments(postOriginId);
        return new ResponseEntity(Response.successResponse(commentResponseDtos), HttpStatus.OK);
    }
}
