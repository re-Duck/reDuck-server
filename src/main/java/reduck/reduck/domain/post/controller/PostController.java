package reduck.reduck.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.post.dto.PostDetailResponseDto;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.dto.TemporaryPostResponse;
import reduck.reduck.domain.post.entity.TemporaryPost;
import reduck.reduck.domain.post.service.PostService;
import reduck.reduck.global.entity.Response;
import reduck.reduck.global.security.CustomUserDetails;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping()
    public ResponseEntity<Void> createPost(@RequestBody @Valid PostDto postDto) {
        postService.createPost(postDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/temporary")
    public ResponseEntity<Void> createTemporaryPost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody @Valid PostDto postDto
    ) {
        postService.createTemporaryPost(customUserDetails.getUser(), postDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/image")
    public ResponseEntity<String> saveImage(@RequestPart(required = false) MultipartFile file) {
        return new ResponseEntity<>(postService.saveMultipartFile(file), HttpStatus.CREATED);
    }

    // 게시글 하나
    @GetMapping("/detail/{postOriginId}")
    public ResponseEntity<PostDetailResponseDto> getPost(@PathVariable String postOriginId) {
        return new ResponseEntity(postService.findByPostOriginId(postOriginId), HttpStatus.OK);
    }

    // 게시글 page수 만큼 조회
    @GetMapping()
    public ResponseEntity<List<PostResponseDto>> getPosts(@RequestParam String postOriginId,
                                                          @RequestParam List<String> postType,
                                                          @RequestParam int page) {
        List<PostResponseDto> postResponseDtos = postService.getPosts(postOriginId, postType, page);
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/temporary")
    public ResponseEntity<Response<List<TemporaryPostResponse>>> getTemporaryPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam Optional<String> temporaryPostOriginId,
            @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        List<TemporaryPostResponse> result = postService.getTemporaryPosts(customUserDetails.getUser(), temporaryPostOriginId, pageable);
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.OK);
    }

    @DeleteMapping("/{postOriginId}")
    public ResponseEntity<Void> removePost(@PathVariable String postOriginId) {
        postService.removePost(postOriginId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{postOriginId}")
    public ResponseEntity<Void> updatePost(@PathVariable String postOriginId, @RequestBody @Valid PostDto postDto) {
        postService.updatePost(postOriginId, postDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}