package reduck.reduck.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.auth.dto.AccessTokenDto;
import reduck.reduck.domain.auth.service.AuthService;
import reduck.reduck.domain.auth.dto.SignInDto;
import reduck.reduck.domain.auth.dto.SignInResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import reduck.reduck.global.entity.Response;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login") // -> /user
    public ResponseEntity<Response<SignInResponseDto>> signIn(@RequestBody @Valid  SignInDto signInDto){
        SignInResponseDto result = authService.signIn(signInDto);
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.OK);
    }
    @GetMapping("/auth/token")
    public ResponseEntity<Response<AccessTokenDto>> refreshAccessToken(HttpServletRequest request) throws Exception {
        AccessTokenDto result = authService.reissuanceAccessToken(request);
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.OK);
    }
}
