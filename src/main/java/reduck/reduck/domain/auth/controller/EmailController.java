package reduck.reduck.domain.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.auth.dto.EmailAuthenticateRequestDto;
import reduck.reduck.domain.auth.dto.EmailAuthenticateResponseDto;
import reduck.reduck.domain.auth.dto.EmailRequestDto;
import reduck.reduck.domain.auth.service.EmailService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email/user/number") //회원가입
    public ResponseEntity<Void> sendUserEmailAuthenticationNumber(@RequestBody @Valid EmailRequestDto userEmailRequestDto) throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail(userEmailRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/email/profile/number") // 마이페이지
    public ResponseEntity<Void> sendEmailAuthenticationNumber(@RequestBody @Valid EmailRequestDto profileEmailRequestDto) throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail(profileEmailRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/email/user") //회원가입
    public ResponseEntity<EmailAuthenticateResponseDto> authenticateUserEmail(@RequestBody @Valid EmailAuthenticateRequestDto userEmailAuthRequestDto) {
        return new ResponseEntity(emailService.authenticateEmail(userEmailAuthRequestDto), HttpStatus.CREATED);
    }
    @PostMapping("/email/profile") //마이 페이지
    public ResponseEntity<EmailAuthenticateResponseDto> authenticateEmail(@RequestBody @Valid EmailAuthenticateRequestDto profileEmailAuthRequestDto) {
        return new ResponseEntity(emailService.authenticateEmail(profileEmailAuthRequestDto), HttpStatus.CREATED);
    }

}
