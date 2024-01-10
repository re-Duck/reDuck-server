package reduck.reduck.domain.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.like.response.PostLikesResponse;
import reduck.reduck.domain.like.service.LikeService;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.global.entity.Response;
import reduck.reduck.global.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/post/{postOriginId}")
    public ResponseEntity<Void> postLike(
            @PathVariable("postOriginId") String postOriginId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        likeService.like(customUserDetails.getuser(), postOriginId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/post")
    public ResponseEntity<Response<List<PostLikesResponse>>> getLikePosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<PostLikesResponse> postLikesResponses = likeService.getLikePosts(customUserDetails.getuser());
        return new ResponseEntity<>(Response.successResponse(postLikesResponses), HttpStatus.OK);
    }
}
