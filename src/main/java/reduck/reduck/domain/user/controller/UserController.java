package reduck.reduck.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.user.dto.SignInResponseDto;
import reduck.reduck.domain.user.dto.signOutDto;
import reduck.reduck.domain.user.dto.SignInDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInDto signInDto) throws Exception {

        return new ResponseEntity<>(userService.signIn(signInDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> signUp(@RequestBody SignUpDto signUpDto) throws Exception {
        return new ResponseEntity<>(userService.signUp(signUpDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Void> signOut(@RequestBody signOutDto signOutDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> withdraw(@PathVariable("userId") String userId) {
        return ResponseEntity.ok().build();

    }
    @GetMapping("/user/get/{userId}")
    public ResponseEntity<SignInResponseDto> getUser(@PathVariable("userId") String userId) throws Exception {
        return new ResponseEntity<>( userService.getUser(userId), HttpStatus.OK);
    }
}
