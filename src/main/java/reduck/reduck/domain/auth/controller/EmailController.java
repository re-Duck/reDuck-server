package reduck.reduck.domain.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.auth.dto.EmailAuthenticateRequestDto;
import reduck.reduck.domain.auth.dto.EmailAuthenticateResponseDto;
import reduck.reduck.domain.auth.dto.EmailRequestDto;
import reduck.reduck.domain.auth.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import reduck.reduck.global.entity.Response;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email/user/number") //회원가입
    public ResponseEntity<Response<Void>> sendUserEmailAuthenticationNumber(@RequestBody @Valid EmailRequestDto userEmailRequestDto) throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail(userEmailRequestDto);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.CREATED);
    }
    @PostMapping("/email/profile/number") // 마이페이지
    public ResponseEntity<Response<Void>> sendEmailAuthenticationNumber(@RequestBody @Valid EmailRequestDto profileEmailRequestDto) throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail(profileEmailRequestDto);
        return new ResponseEntity<>(Response.successResponse(), HttpStatus.CREATED);
    }
    @PostMapping("/email/user") //회원가입
    public ResponseEntity<Response<EmailAuthenticateResponseDto>> authenticateUserEmail(@RequestBody @Valid EmailAuthenticateRequestDto userEmailAuthRequestDto) {
        EmailAuthenticateResponseDto result = emailService.authenticateEmail(userEmailAuthRequestDto);
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.CREATED);

    }
    @PostMapping("/email/profile") //마이 페이지
    public ResponseEntity<Response<EmailAuthenticateResponseDto>> authenticateEmail(@RequestBody @Valid EmailAuthenticateRequestDto profileEmailAuthRequestDto) {
        EmailAuthenticateResponseDto result = emailService.authenticateEmail(profileEmailAuthRequestDto);
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.CREATED);
    }
}
