package reduck.reduck.domain.follow.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reduck.reduck.domain.follow.dto.FollowDto;
import reduck.reduck.domain.follow.service.FollowService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<Void> follow(
            @RequestBody FollowDto followDto
    ) {
        followService.follow(followDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
