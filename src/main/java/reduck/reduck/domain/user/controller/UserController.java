package reduck.reduck.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.jwt.service.JwtService;
import reduck.reduck.domain.user.dto.SignInResponseDto;
import reduck.reduck.domain.user.dto.SignOutDto;
import reduck.reduck.domain.user.dto.SignInDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user") // -> /user
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInDto signInDto) throws Exception {
        return new ResponseEntity<>(userService.signIn(signInDto), HttpStatus.OK);
    }

    @PostMapping("/user/{userId}") // -> /user/{userId}
    public ResponseEntity<Boolean> signUp(@RequestBody SignUpDto signUpDto) throws Exception {
        return new ResponseEntity<>(userService.signUp(signUpDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Void> signOut(@RequestBody SignOutDto signOutDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/{userID}")
    public ResponseEntity<Void> withdraw(@PathVariable("userId") String userId) {
        return ResponseEntity.ok().build();

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<SignInResponseDto> getUser(@PathVariable("userId") String userId) throws Exception {
        return new ResponseEntity<>( userService.getUser(userId), HttpStatus.OK);
    }
}
