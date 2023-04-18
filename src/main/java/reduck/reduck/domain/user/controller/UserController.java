package reduck.reduck.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.user.dto.SignOutDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/{userId}") // -> /user/{userId}
        public ResponseEntity<User> signUp(@RequestPart @Valid SignUpDto signUpDto, @RequestPart(required = false) MultipartFile multipartFile) throws Exception {
        System.out.println("signUpDto = " + signUpDto.getPassword());
        userService.signUp(signUpDto, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Void> signOut(@RequestBody SignOutDto signOutDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin/{userID}")
    public ResponseEntity<Void> withdraw(@PathVariable("userId") String userId) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") String userId) {
        return new ResponseEntity<>( userService.findByUserId(userId), HttpStatus.OK);
    }

}
