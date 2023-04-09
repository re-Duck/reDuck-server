package reduck.reduck.domain.auth.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.auth.entity.RefreshToken;
import reduck.reduck.domain.auth.repository.AuthRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthRepository authRepository;
    @Test
    @Transactional
    void saveRefreshToken() {
        //given
        Optional<User> user = userRepository.findByUserId("test1");
        authService.saveRefreshToken("refresh_token_save_test", user.get());
        //when
        Optional<RefreshToken> refreshToken = authRepository.findById(1L);
        //then
        Assertions.assertThat(refreshToken.get().getRefreshToken()).isEqualTo("refresh_token_save_test");
        Assertions.assertThat(refreshToken.get().getUser()).isEqualTo(user.get());
    }

    @Test
    @Transactional
    void reissuanceAccessToken() throws Exception {
        HttpServletRequest request = null;
        request.setAttribute("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MSIsInJvbGVzIjpbeyJuYW1lIjoiUk9MRV9VU0VSIn1dLCJpYXQiOjE2ODAxMDAyNTgsImV4cCI6MTY4MTMwOTg1OH0.EvGXCdcrAG0LbhQwohWAqwq2fUEJrIgzGALFCBivsJA");

        authService.reissuanceAccessToken(request, "test1");
        //given
        //when
        //then
    }

    @Test
    @Transactional
    void getRefreshToken() {
    }
}