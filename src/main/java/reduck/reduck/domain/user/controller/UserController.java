package reduck.reduck.domain.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.user.dto.ModifyUserDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.dto.UserInfoDtoRes;
import reduck.reduck.domain.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<Void> signUp(@RequestPart @Valid SignUpDto signUpDto, @RequestPart(required = false) MultipartFile file) throws Exception {
        userService.signUp(signUpDto, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/duplicate/{userId}")
    public ResponseEntity<Boolean> isDuplicateUserId(@PathVariable String userId) throws Exception {
        return new ResponseEntity<>(userService.isDuplicatedUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> withdraw() {
        userService.withdraw();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/me")
    public ResponseEntity<UserInfoDtoRes> getMyInfo() {
        return new ResponseEntity(userService.getMyInfo(), HttpStatus.OK);
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
