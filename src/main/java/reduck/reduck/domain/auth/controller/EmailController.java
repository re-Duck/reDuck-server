package reduck.reduck.domain.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.auth.dto.CompanyEmailRequestDto;
import reduck.reduck.domain.auth.dto.EmailRequestDto;
import reduck.reduck.domain.auth.dto.SchoolEmailRequestDto;
import reduck.reduck.domain.auth.dto.UserEmailRequestDto;
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
    public ResponseEntity<Void> sendEmailAuthenticationNumber(@RequestBody @Valid EmailRequestDto emailRequestDto) throws MessagingException, UnsupportedEncodingException {
        emailService.sendEmail(emailRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/email/user")
    public ResponseEntity<Void> authenticateUserEmail( @RequestBody @Valid UserEmailRequestDto userEmailRequestDto) {
        System.out.println("userEmailRequestDto = " + userEmailRequestDto.getNumber());
        emailService.authenticateUserEmail(userEmailRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/email/company")
    public ResponseEntity<Void> authenticateCompanyEmail(@RequestBody @Valid CompanyEmailRequestDto companyEmailRequestDto) {
        emailService.authenticateCompanyEmail(companyEmailRequestDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/email/school")
    public ResponseEntity<Void> authenticateSchoolEmail(@RequestBody @Valid SchoolEmailRequestDto schoolEmailRequestDto) {
        emailService.authenticateSchoolEmail(schoolEmailRequestDto);
        return ResponseEntity.ok().build();
    }

}
