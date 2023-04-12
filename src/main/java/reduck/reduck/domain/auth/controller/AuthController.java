package reduck.reduck.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.auth.dto.AccessTokenDto;
import reduck.reduck.domain.auth.service.AuthService;
import reduck.reduck.domain.auth.dto.SignInDto;
import reduck.reduck.domain.auth.dto.SignInResponseDto;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login") // -> /user
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInDto signInDto, HttpServletRequest request){
        return new ResponseEntity<>(authService.signIn(signInDto), HttpStatus.OK);
    }
    @GetMapping("/auth/{userId}/token")
    public ResponseEntity<AccessTokenDto> refreshAccessToken(HttpServletRequest request, @PathVariable("userId") String userId) throws Exception {
        return new ResponseEntity<>(authService.reissuanceAccessToken(request, userId), HttpStatus.OK);
    }
}
