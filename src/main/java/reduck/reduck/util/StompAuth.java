package reduck.reduck.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reduck.reduck.global.security.JwtProvider;

@Component

public class StompAuth {
    private static JwtProvider jwtProvider;

    public static String getAccount(String token) {
        return jwtProvider.getAccount(token);
    }

    @Bean
    public JwtProvider provider(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        return jwtProvider;
    }
}
