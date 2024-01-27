package reduck.reduck.domain.scrap.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reduck.reduck.domain.scrap.service.ScrapService;
import reduck.reduck.global.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scrap")
public class ScrapController {
    private final ScrapService scrapService;

    @PostMapping("/post/{postOriginId}")
    public ResponseEntity<Void> scrapPost(
            @PathVariable("postOriginId") String postOriginId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        scrapService.scrapPost(customUserDetails.getUser(),postOriginId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
