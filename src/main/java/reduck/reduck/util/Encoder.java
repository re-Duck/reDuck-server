package reduck.reduck.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reduck.reduck.domain.user.dto.ModifyUserDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.UserException;
import reduck.reduck.global.security.SecurityConfig;

@Component
public class Encoder{
    private static PasswordEncoder passwordEncoder;

    private PasswordEncoder passwordEncoder2;
    private void Encoder() {
        throw new RuntimeException("Encoder 인스턴스 생성 불가");
    }

    public static void encodePasswordOf(SignUpDto signUpDto) {
        String password = signUpDto.getPassword();
        String encode = passwordEncoder.encode(password);
        signUpDto.setPassword(encode);

    }

    public static void encodePasswordOf(ModifyUserDto modifyUserDto) {

        String password = modifyUserDto.getPassword();
        String encode = passwordEncoder.encode(password);
        modifyUserDto.setPassword(encode);
    }

    public static boolean validate(String raw, String encoded) {

        if (!passwordEncoder.matches(raw,encoded)) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }
        return true;
    }
    @Bean
    public PasswordEncoder Encoding(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return passwordEncoder;
    }
}
