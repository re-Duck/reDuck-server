package reduck.reduck.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.post.dto.*;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.post.service.PostService;
import reduck.reduck.global.entity.Response;
import reduck.reduck.global.exception.ErrorResponse;
import reduck.reduck.global.security.CustomUserDetails;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Slf4j
public class PostController {
    private final PostService postService;

    @Operation(summary = "단일 게시글 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404 POST_NOT_EXIST", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/detail/{postOriginId}")
    public ResponseEntity<Response<PostDetailResponseDto>> getPost(
            @PathVariable("postOriginId") String postOriginId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        PostDetailResponseDto result = postService.findByPostOriginId(postOriginId, customUserDetails.getUser());
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.OK);
    }

    @Operation(summary = "게시글 스크롤 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "스크롤 조회 성공"),
            @ApiResponse(responseCode = "404 POST_NOT_EXIST", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Response<List<PostResponseDto>>> getPosts(
            @RequestParam(value = "postOriginId", required = false) String postOriginId,
            @Parameter(schema = @Schema(implementation = PostType.class)) @RequestParam(value = "postType", required = false) List<String> postType,
            @RequestParam(value = "page", required = false, defaultValue = "10") int page
    ) {
        List<PostResponseDto> postResponseDtos = postService.getPosts(postOriginId, postType, page);
        return new ResponseEntity<>(Response.successResponse(postResponseDtos), HttpStatus.OK);
    }

    @Operation(summary = "게시글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 생성 성공"),
            @ApiResponse(responseCode = "409 CONFLICT", description = "postOriginId 값 중복",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Response<Void>> createPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid PostDto postDto) {
        postService.createPost(customUserDetails.getUser(), postDto);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.CREATED);
    }

    @Operation(summary = "게시글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "403 FORBIDDEN", description = "게시글에 대한 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404 POST_NOT_EXIST", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{postOriginId}")
    public ResponseEntity<Response<Void>> removePost(
            @Parameter(name = "postOriginId") @PathVariable String postOriginId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        postService.removePost(postOriginId, customUserDetails.getUser());
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "게시글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "403 FORBIDDEN", description = "게시글에 대한 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404 POST_NOT_EXIST", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{postOriginId}")
    public ResponseEntity<Response<Void>> updatePost(
            @Parameter(name = "postOriginId") @PathVariable String postOriginId,
            @RequestBody @Valid PostDto postDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        postService.updatePost(postOriginId, postDto, customUserDetails.getUser());
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.OK);
    }

    @Operation(summary = "게시글 이미지 저장")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "이미지 저장 성공"),
            @ApiResponse(responseCode = "500 INTERNAL_SERVER_ERROR", description = "이미지 저장 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/image")
    public ResponseEntity<Response<ImagePathResponse>> saveImage
            (
                    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = MultipartFile.class)))
                    @RequestPart(required = false) MultipartFile file
            ) {
        String imagePath = postService.saveMultipartFile(file);
        ImagePathResponse imagePathResponse = new ImagePathResponse(imagePath);
        return new ResponseEntity<>(Response.successResponse(imagePathResponse), HttpStatus.CREATED);
    }

    @Operation(summary = "임시 게시글 목록 조회 -> 추후 스크롤 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "임시 게시글 목록 조회 성공")
    })
    @GetMapping("/temporary")
    public ResponseEntity<Response<List<TemporaryPostResponse>>> getTemporaryPosts(
            @Valid @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam("temporaryPostOriginId") Optional<String> temporaryPostOriginId,
            @Parameter(name = "pageable") @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<TemporaryPostResponse> result = postService.getTemporaryPosts(customUserDetails.getUser(), temporaryPostOriginId, pageable);
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.OK);
    }

    @Operation(summary = "임시 게시글 단일 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 단일 조회 성공"),
            @ApiResponse(responseCode = "404 POST_NOT_EXIST", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/temporary/{temporaryPostOriginId}")
    public ResponseEntity<Response<TemporaryPostResponse>> getTemporaryPost(
            @PathVariable("temporaryPostOriginId") String temporaryPostOriginId
    ) {
        TemporaryPostResponse result = postService.getTemporaryPost(temporaryPostOriginId);
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.OK);
    }

    @Operation(summary = "임시 게시글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "임시 게시글 생성 성공"),
            @ApiResponse(responseCode = "409 CONFLICT", description = "postOriginId 값 중복",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/temporary")
    public ResponseEntity<Response<Void>> createTemporaryPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid PostDto postDto
    ) {
        postService.createTemporaryPost(customUserDetails.getUser(), postDto);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.CREATED);
    }

    @Operation(summary = "임시 게시글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "403 FORBIDDEN", description = "게시글에 대한 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404 POST_NOT_EXIST", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/temporary/{temporaryPostOriginId}")
    public ResponseEntity<Response<Void>> deleteTemporaryPost(
            @PathVariable("temporaryPostOriginId") String temporaryPostOriginId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        postService.removeTemporaryPost(customUserDetails.getUser(), temporaryPostOriginId);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "임시 게시글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "임시 게시글 수정 성공"),
            @ApiResponse(responseCode = "403 FORBIDDEN", description = "게시글에 대한 권한 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404 POST_NOT_EXIST", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/temporary/{temporaryPostOriginId}")
    public ResponseEntity<Response<Void>> updateTemporaryPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("temporaryPostOriginId") String temporaryPostOriginId,
            @RequestBody @Valid PostDto postDto
    ) {
        postService.updateTemporaryPost(customUserDetails.getUser(), temporaryPostOriginId, postDto);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.OK);
    }
}