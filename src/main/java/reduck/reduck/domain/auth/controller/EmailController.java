package reduck.reduck.domain.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import reduck.reduck.domain.auth.dto.EmailDtoReq;
import reduck.reduck.domain.auth.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;


    @PostMapping("/auth/email")
    public ResponseEntity<Void> sendEmailAuthenticationNumber(@RequestBody EmailDtoReq emailDtoReq) throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail(emailDtoReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/email/{number}")
    public ResponseEntity<Void> authenticateNumber(@PathVariable int number, @RequestBody EmailDtoReq emailDtoReq ) {
        emailService.authenticateNumber(number, emailDtoReq);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
