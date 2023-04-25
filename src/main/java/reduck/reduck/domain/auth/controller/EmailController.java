package reduck.reduck.domain.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.auth.dto.EmailDtoReq;
import reduck.reduck.domain.auth.service.EmailService;
import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<Void> sendEmailAuthenticationNumber(@RequestBody EmailDtoReq emailDtoReq) throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail(emailDtoReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/email/{number}")
    public ResponseEntity<Void> authenticateEmail(@PathVariable int number, @RequestBody @Valid EmailDtoReq emailDtoReq ) {
        emailService.authenticateEmail(number, emailDtoReq);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/email/{userId}/company/{companyEmail}/{number}")
    public ResponseEntity<Void> authenticateCompanyEmail(@PathVariable("userId") String userId, @PathVariable("companyEmail") String companyEmail, @PathVariable("number") int number) {
        emailService.authenticateCompanyEmail(userId, companyEmail, number);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/email/{userId}/school/{schoolEmail}/{number}")
    public ResponseEntity<Void> authenticateSchoolEmail(@PathVariable("userId") String userId, @PathVariable("schoolEmail") String schoolEmail, @PathVariable("number") int number) {
        emailService.authenticateSchoolEmail(userId, schoolEmail, number);
        return ResponseEntity.ok().build();
    }

}
