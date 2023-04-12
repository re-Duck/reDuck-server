package reduck.reduck.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.service.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService boardService;

    @PostMapping("/board/{postOriginId}")
    public ResponseEntity<Void> createPost(HttpServletRequest request, @RequestPart PostDto postDto, @RequestPart(required = false) List<MultipartFile> multipartFiles) {
        boardService.createPost(postDto, multipartFiles);
        return ResponseEntity.ok().build();
    }



    // 게시글 하나
    @GetMapping("/board/{postOriginId}")
    public ResponseEntity<Void> getPost(@PathVariable String postOriginId) {
        boardService.findByPostOriginId(postOriginId);
        return ResponseEntity.ok().build();
    }

    // 게시글 type에 해당하는 최신의 page갯수 만큼 => 첫 메인에 보여질 피드들
    @GetMapping("/board/{postType}/{page}")
    public ResponseEntity<Void> getPosts(@PathVariable String postType, @PathVariable int page) {
        boardService.findAllByPostTypeWithPage(postType);
        return ResponseEntity.ok().build();
    }

    // 게시글 type에 해당하는 게시글 기준으로 page갯수 만큼 => 각 게시판의 스크롤 한 경우.
    @GetMapping("/board/{postType}/{postOriginId}/{page}")
    public ResponseEntity<Void> getPosts(@PathVariable String postType, @PathVariable String postOriginId, @PathVariable int page) {
        boardService.findAllByPostTypeWithOriginIdAndPage(postOriginId);
        return ResponseEntity.ok().build();
    }

}
