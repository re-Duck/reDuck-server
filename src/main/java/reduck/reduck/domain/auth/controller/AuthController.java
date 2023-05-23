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
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login") // -> /user
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody @Valid  SignInDto signInDto){
        return new ResponseEntity<>(authService.signIn(signInDto), HttpStatus.OK);
    }
    @GetMapping("/auth/token")
    public ResponseEntity<AccessTokenDto> refreshAccessToken(HttpServletRequest request) throws Exception {
        return new ResponseEntity<>(authService.reissuanceAccessToken(request), HttpStatus.OK);
    }

}
