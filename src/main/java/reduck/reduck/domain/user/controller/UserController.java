package reduck.reduck.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import reduck.reduck.domain.jwt.dto.AccessTokenDto;
import reduck.reduck.domain.jwt.service.JwtService;
import reduck.reduck.domain.user.dto.SignInResponseDto;
import reduck.reduck.domain.user.dto.SignOutDto;
import reduck.reduck.domain.user.dto.SignInDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.entity.UserProfileImg;
import reduck.reduck.domain.user.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    @PostMapping("/user") // -> /user
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInDto signInDto) throws Exception {
        return new ResponseEntity<>(userService.signIn(signInDto), HttpStatus.OK);
    }

    @PostMapping("/user/{userId}") // -> /user/{userId}
        public ResponseEntity<Void> signUp(@RequestPart SignUpDto signUpDto, @RequestPart MultipartFile multipartFile) throws Exception {
        userService.signUp  (signUpDto, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
//    @PostMapping("/user/image")
//    public ResponseEntity<UserProfileImg> saveImage(@RequestPart(required = false) MultipartFile multipartFile) throws ServletException, IOException {
//        UserProfileImg userProfileImg = userService.saveImage(multipartFile);
//        return new ResponseEntity(userProfileImg, HttpStatus.CREATED);
//    }

    @GetMapping
    public ResponseEntity<Void> signOut(@RequestBody SignOutDto signOutDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/{userID}")
    public ResponseEntity<Void> withdraw(@PathVariable("userId") String userId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") String userId) throws Exception {
        return new ResponseEntity<>( userService.getUser(userId), HttpStatus.OK);
    }
    @GetMapping("/user/{userId}/token")
    public ResponseEntity<AccessTokenDto> refreshAccessToken(HttpServletRequest request, @PathVariable("userId") String userId) throws Exception {
        return new ResponseEntity<>(jwtService.reissuanceAccessToken(request, userId), HttpStatus.OK);
    }
}
