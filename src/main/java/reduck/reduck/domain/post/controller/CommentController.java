package reduck.reduck.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import reduck.reduck.global.exception.ErrorResponse;
import reduck.reduck.global.security.CustomUserDetails;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 생성 완료"),
            @ApiResponse(responseCode = "404 POST_NOT_EXIST", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409 CONFLICT", description = "commentOriginId 값 중복",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/post/comment")
    public ResponseEntity<Response<Void>> createComment(
            @RequestBody @Valid CommentDto commentDto
    ) {
        commentService.createComment(commentDto);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.CREATED);
    }

    @Operation(summary = "대댓글(답글) 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "답글 생성 완료"),
            @ApiResponse(responseCode = "404 POST_NOT_EXIST", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409 CONFLICT", description = "commentOriginId 값 중복",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/comments/posts/reply")
    public ResponseEntity<Response<Void>> createReplyComment(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid CommentDto commentDto) {
        commentService.createReplyComment(customUserDetails.getUser(), commentDto);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.CREATED);
    }

    @Operation(summary = "댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 삭제 완료"),
            @ApiResponse(responseCode = "403 FORBIDDEN", description = "댓글에 대한 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404 COMMENT_NOT_EXIST", description = "댓글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/post/comment/{commentOriginId}")
    public ResponseEntity<Response<Void>> removeComment(
            @PathVariable("commentOriginId") String commentOriginId
    ) {
        commentService.removeComment(commentOriginId);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 완료"),
            @ApiResponse(responseCode = "403 FORBIDDEN", description = "댓글에 대한 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404 COMMENT_NOT_EXIST", description = "댓글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/post/comment/{commentOriginId}")
    public ResponseEntity<Response<Void>> updateComment(
            @PathVariable("commentOriginId") String commentOriginId,
            @RequestBody @Valid UpdateCommentDto commentDto
    ) {
        commentService.updateComment(commentOriginId, commentDto);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.OK);
    }
    @Operation(summary = "게시글의 댓글 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 완료"),
            @ApiResponse(responseCode = "404 POST_NOT_EXIST", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/post/{postOriginId}/comments")
    public ResponseEntity<Response<List<CommentResponseDto>>> getComments(
            @PathVariable("postOriginId") String postOriginId
    ) {
        List<CommentResponseDto> commentResponseDtos = commentService.getComments(postOriginId);
        return new ResponseEntity(Response.successResponse(commentResponseDtos), HttpStatus.OK);
    }
}
