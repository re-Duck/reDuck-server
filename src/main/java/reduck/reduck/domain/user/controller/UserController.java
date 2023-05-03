package reduck.reduck.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.user.dto.ModifyUserDto;
import reduck.reduck.domain.user.dto.SignOutDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.dto.UserInfoDtoRes;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping() // -> /user/{userId}
    public ResponseEntity<Void> signUp(@RequestPart @Valid SignUpDto signUpDto, @RequestPart(required = false) MultipartFile file) throws Exception {
        userService.signUp(signUpDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/duplicate/{userId}")
    public ResponseEntity<Boolean> isDuplicateUserId(@PathVariable String userId) throws Exception {
        return new ResponseEntity<>(userService.isDuplicatedUserId(userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Void> signOut(@RequestBody SignOutDto signOutDto) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> withdraw(@PathVariable("userId") String userId) {
        userService.withdraw(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoDtoRes> getUser(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> modifyUserInfo(@RequestPart @Valid ModifyUserDto modifyUserDto, @RequestPart(required = false) MultipartFile multipartFile) {
        userService.modifyUserInfo(modifyUserDto, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
