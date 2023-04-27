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
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.AuthErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.AuthException;
import reduck.reduck.global.exception.exception.UserException;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailAuthenticationRepository emailAuthenticationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String reduck;

    @Transactional
    public void sendEmail(EmailDtoReq emailDtoReq) throws MessagingException, UnsupportedEncodingException {
        int emailCertificatedNumber = createEmailCertificatedNumber(); //인증번호.
        MimeMessage message = craeteEmailTemplate(emailDtoReq, emailCertificatedNumber);
        EmailAuthentication emailAuthentication = EmailAuthentication.builder()
                .email(emailDtoReq.getEmail())
                .authenticationNumber(emailCertificatedNumber)
                .build();
        try {
            emailAuthenticationRepository.save(emailAuthentication);

            // 이메일 발신
            javaMailSender.send(message);

        } catch (Exception e) {
            System.out.println("e = " + e);
            throw e;
        }
    }

    @Transactional
    public void authenticateEmail(int number, EmailDtoReq emailDtoReq) {

        Optional<EmailAuthentication> emailAuthentication = emailAuthenticationRepository.findTopByEmailOrderByIdDesc(emailDtoReq.getEmail());
        validateEmailAuthenticationNumber(emailAuthentication, number);

    }

    @Transactional
    public void authenticateCompanyEmail(String userId, String companyEmail, int number) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
        Optional<EmailAuthentication> emailAuthentication = emailAuthenticationRepository.findTopByEmailOrderByIdDesc(companyEmail);
        if (validateEmailAuthenticationNumber(emailAuthentication, number)) {
            user.authenticatedCompanyEmail();
            userRepository.save(user);
            return;
        }
    }

    @Transactional
    public void authenticateSchoolEmail(String userId, String schoolEmail, int number) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
        Optional<EmailAuthentication> emailAuthentication = emailAuthenticationRepository.findTopByEmailOrderByIdDesc(schoolEmail);
        if (validateEmailAuthenticationNumber(emailAuthentication, number)) {
            user.authenticatedSchoolEmail();
            userRepository.save(user);
            return;
        }
    }


    private MimeMessage craeteEmailTemplate(EmailDtoReq emailDtoReq, int emailCertificatedNumber) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, emailDtoReq.getEmail());
        message.setSubject("[reDuck] 환영합니다. 이메일 인증을 완료해주세요.");
        message.setText(emailHtml(emailCertificatedNumber), "utf-8", "html");
        message.setFrom(new InternetAddress(reduck, "reDuck"));
        return message;
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

    private boolean validateEmailAuthenticationNumber(Optional<EmailAuthentication> emailAuthentication, int number) {
        LocalDateTime expire = LocalDateTime.now();

        if (emailAuthentication.get().getAuthenticationNumber() == number) {
            if (emailAuthentication.get().getCreatedAt().plusMinutes(5).isAfter(expire)) {
                return true;
            }
            //일치하지만 시간 만료
            throw new AuthException(AuthErrorCode.EXPIRED_AUTHENTICATION_NUMBER);
        }
        //일치하지 않음.
        throw new AuthException((AuthErrorCode.AUTHENTICATON_NUMBER_NOT_MATCH));
    }

}
