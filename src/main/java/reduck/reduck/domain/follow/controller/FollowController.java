package reduck.reduck.domain.follow.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.follow.dto.FollowRequest;
import reduck.reduck.domain.follow.dto.FollowerResponse;
import reduck.reduck.domain.follow.service.FollowService;
import reduck.reduck.global.entity.Response;

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

    @GetMapping
    public ResponseEntity<Response<List<FollowerResponse>>> getFollowers() {
        List<FollowerResponse> followers = followService.getFollowers();
        Response<List<FollowerResponse>> response = Response.successResponse(followers);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
