package reduck.reduck.domain.follow.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.follow.dto.FollowRequest;
import reduck.reduck.domain.follow.dto.FollowerResponse;
import reduck.reduck.domain.follow.service.FollowService;
import reduck.reduck.global.entity.Response;
import reduck.reduck.global.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<Void> follow(@RequestBody FollowRequest followDto) {
        followService.follow(followDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{followingId}")
    public ResponseEntity<Void> cancel(
            @PathVariable("followingId") String followingUserId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        followService.cancel(customUserDetails.getuser(), followingUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/followers")
    public ResponseEntity<Response<List<FollowerResponse>>> getFollowers() {
        List<FollowerResponse> followers = followService.getFollowers();
        Response<List<FollowerResponse>> response = Response.successResponse(followers);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/followings")
    public ResponseEntity<Response<List<FollowerResponse>>> getFollowings() {
        List<FollowerResponse> followings = followService.getFollowings();
        Response<List<FollowerResponse>> response = Response.successResponse(followings);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
