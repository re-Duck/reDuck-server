package reduck.reduck.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.dto.pathDto;
import reduck.reduck.domain.post.service.PostService;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    @PostMapping("/image")
    public ResponseEntity<String> saveImage(@RequestPart(required = false) MultipartFile file) {
        return new ResponseEntity<>(postService.saveMultipartFile(file), HttpStatus.CREATED);
    }

    // 게시글 하나
    @GetMapping("/detail/{postOriginId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable String postOriginId) {
        return new ResponseEntity(postService.findByPostOriginId(postOriginId), HttpStatus.OK);
    }

    // 게시글 type에 해당하는 최신의 page갯수 만큼 => 첫 메인에 보여질 피드들
    @GetMapping()
    public ResponseEntity<List<PostResponseDto>> getPosts(@RequestParam List<String> postType, @RequestParam int page) {
        List<PostResponseDto> postResponseDtos = postService.findPostAllByPostTypeWithPage(postType, page);
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/content")
    public ResponseEntity<InputStreamResource> getPostContent(@RequestBody pathDto dto) throws FileNotFoundException {

        Path filePath = Paths.get(dto.getPostContentPath());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(filePath.toString()));
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);

    }


    // 게시글 type에 해당하는 게시글 기준으로 page갯수 만큼 => 각 게시판의 스크롤 한 경우.
    @GetMapping("/{postOriginId}")
    public ResponseEntity<List<PostResponseDto>> getPosts(@PathVariable String postOriginId, @RequestParam String postType, @RequestParam int page) {
        List<PostResponseDto> postResponseDtos = postService.findAllByPostTypeAndPostOriginIdOrderByIdDescLimitPage(postType, postOriginId, page);
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{postOriginId}")
    public ResponseEntity<Void> removePost(@PathVariable String postOriginId) {
        postService.removePost(postOriginId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}