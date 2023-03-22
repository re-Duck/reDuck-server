package reduck.reduck.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.user.dto.signOutDto;
import reduck.reduck.domain.user.dto.SignInDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @PostMapping("/{userId}")
    public ResponseEntity<Void> signIn(@RequestBody SignInDto signInDto) {
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<Void> signUp(@RequestBody SignUpDto signUpDto) {
        userService.signUp(signUpDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> signOut(@RequestBody signOutDto signOutDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> withdraw(@PathVariable("userId") String userId) {
        return ResponseEntity.ok().build();

    }
    @GetMapping("/{userId}/info")
    public ResponseEntity<Void> getInfo(@PathVariable("userId") String userId) {
        return ResponseEntity.ok().build();
    }

}
