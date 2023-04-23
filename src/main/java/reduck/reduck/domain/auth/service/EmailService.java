package reduck.reduck.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import reduck.reduck.domain.auth.dto.EmailDtoReq;
import reduck.reduck.domain.auth.entity.EmailAuthentication;
import reduck.reduck.domain.auth.repository.EmailAuthenticationRepository;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.exception.CommonException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailAuthenticationRepository emailAuthenticationRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String reduck;

    @Transactional
    public void sendEmail(EmailDtoReq emailDtoReq) throws MessagingException, UnsupportedEncodingException {
        int emailCertificatedNumber = createEmailCertificatedNumber(); //인증번호.
        // 이메일 발신될 데이터 적재

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, emailDtoReq.getEmail());
        message.setSubject("[reDuck] 환영합니다. 이메일 인증을 완료해주세요.");
        message.setText(emailHtml(emailCertificatedNumber), "utf-8", "html");
        message.setFrom(new InternetAddress(reduck, "reDuck"));
        // 이메일 발신
        EmailAuthentication emailAuthentication = EmailAuthentication.builder()
                .email(emailDtoReq.getEmail())
                .authenticationNumber(emailCertificatedNumber)
                .build();
        try {
            emailAuthenticationRepository.save(emailAuthentication);

        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        javaMailSender.send(message);

    }
    @Transactional
    public void authenticateNumber(int number, EmailDtoReq emailDtoReq) {

        Optional<EmailAuthentication> emailAuthentication = emailAuthenticationRepository.findTopByEmailOrderByIdDesc(emailDtoReq.getEmail());

        if (emailAuthentication.get().getAuthenticationNumber()!= number) {
            throw new CommonException(CommonErrorCode.IS_NOT_MATCH);
        }
    }

    private String emailHtml(int emailCertificatedNumber) {
        Context context = new Context();
        context.setVariable("code", emailCertificatedNumber);
        return templateEngine.process("email/email", context); //mail.html
    }

    private int createEmailCertificatedNumber() {
        Random random = new Random();
        return random.nextInt(888888) + 111111;

    }

}
