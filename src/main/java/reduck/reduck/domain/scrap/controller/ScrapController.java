package reduck.reduck.domain.scrap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.post.dto.TemporaryPostResponse;
import reduck.reduck.domain.scrap.dto.ScrapPostDto;
import reduck.reduck.domain.scrap.service.ScrapService;
import reduck.reduck.global.entity.Response;
import reduck.reduck.global.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scrap")
public class ScrapController {
    private final ScrapService scrapService;

    @PostMapping("/posts/{postOriginId}")
    public ResponseEntity<Void> scrapPost(
            @PathVariable("postOriginId") String postOriginId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        scrapService.scrapPost(customUserDetails.getUser(), postOriginId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/posts")
    public ResponseEntity<Response<List<ScrapPostDto>>> getScrapPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<ScrapPostDto> result = scrapService.getScrapPosts(customUserDetails.getUser());
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.OK);
    }

    @GetMapping("/posts/{postOriginId}/status")
    public ResponseEntity<Response<Boolean>> getScrapPostStatus(
            @PathVariable("postOriginId") String postOriginId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Boolean result = scrapService.getScrapPostStatus(customUserDetails.getUser(), postOriginId);
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.OK);
    }
}
