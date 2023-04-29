package reduck.reduck.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.service.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/post/{postOriginId}")
    public ResponseEntity<Void> createPost( @RequestPart PostDto postDto, @RequestPart(required = false) List<MultipartFile> multipartFiles) {
        boolean empty = multipartFiles.isEmpty();
        System.out.println("empty = " + empty);
        postService.createPost(postDto, multipartFiles);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 게시글 하나
    @GetMapping("/post/{postOriginId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable String postOriginId) {
        return new ResponseEntity(postService.findByPostOriginId(postOriginId), HttpStatus.OK);
    }

    // 게시글 type에 해당하는 최신의 page갯수 만큼 => 첫 메인에 보여질 피드들
    @GetMapping("/post/{postType}/{page}")
    public ResponseEntity<List<PostResponseDto>> getPosts(@PathVariable String postType, @PathVariable int page) {
        List<PostResponseDto> postResponseDtos = postService.findPostAllByPostTypeWithPage(postType, page);
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }

    // 게시글 type에 해당하는 게시글 기준으로 page갯수 만큼 => 각 게시판의 스크롤 한 경우.
    @GetMapping("/post/{postType}/{postOriginId}/{page}")
    public ResponseEntity<List<PostResponseDto>> getPosts(@PathVariable String postType, @PathVariable String postOriginId, @PathVariable int page) {
        List<PostResponseDto> postResponseDtos = postService.findAllByPostTypeAndPostOriginIdOrderByIdDescLimitPage(postType, postOriginId, page);
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }

}
