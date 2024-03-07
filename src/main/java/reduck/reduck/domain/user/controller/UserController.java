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

import jakarta.validation.Valid;
import reduck.reduck.global.entity.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<Response<Void>> signUp(
            @RequestPart @Valid SignUpDto signUpDto, @RequestPart(required = false) MultipartFile file
    ) {
        userService.signUp(signUpDto, file);
        return new ResponseEntity(Response.successResponse(), HttpStatus.CREATED);
    }

    @GetMapping("/duplicate/{userId}")
    public ResponseEntity<Response<Boolean>> isDuplicateUserId(@PathVariable String userId) {
        boolean result = userService.isDuplicatedUserId(userId);
        return new ResponseEntity(Response.successResponse(result), HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<Response<Void>> withdraw() {
        userService.withdraw();
        return new ResponseEntity(Response.successResponse(), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response<UserInfoDtoRes>> getUser(@PathVariable("userId") String userId) {
        UserInfoDtoRes result = userService.getUser(userId);
        return new ResponseEntity(Response.successResponse(result), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Response<UserInfoDtoRes>> modifyUserInfo(
            @RequestPart @Valid ModifyUserDto modifyUserDto,
            @RequestPart(required = false) MultipartFile file
    ) {
        UserInfoDtoRes result = userService.modifyUserInfo(modifyUserDto, file);
        return new ResponseEntity(Response.successResponse(result), HttpStatus.CREATED);
    }
}
