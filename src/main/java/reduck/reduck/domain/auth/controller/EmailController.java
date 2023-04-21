package reduck.reduck.domain.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import reduck.reduck.domain.auth.dto.EmailDtoReq;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    @PostMapping("/auth/email")
    public ResponseEntity<Object> send(@RequestBody EmailDtoReq emailDtoReq) throws MessagingException {
        // 이메일 발신될 데이터 적재
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO,emailDtoReq.getEmail());
        message.setSubject("reDuck");
        message.setText(html(), "utf-8","html");

        // 이메일 발신
        javaMailSender.send(message);
        System.out.println("message = " + message);
        // 결과 반환
        return ResponseEntity.ok(true);
    }
    @GetMapping("/auth/html")
    public String html() {
        Context context = new Context();
        context.setVariable("code", 123153);
//        model.addAttribute("code", 123456);

        return templateEngine.process("email/email", context); //mail.html
    }
}
