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

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email/number") // 인증번호 요청
    public ResponseEntity<Void> sendUserEmailAuthenticationNumber(@RequestBody @Valid EmailRequestDto emailRequestDto) throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail(emailRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/email") // 인증 요청
    public ResponseEntity<EmailAuthenticateResponseDto> authenticateUserEmail(@RequestBody @Valid EmailAuthenticateRequestDto emailRequestDto) {
        return new ResponseEntity(emailService.authenticateEmail(emailRequestDto), HttpStatus.CREATED);
    }


}
