package reduck.reduck.domain.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.jwt.entity.RefreshToken;
import reduck.reduck.domain.jwt.repository.JwtRepository;
import reduck.reduck.domain.user.dto.SignInResponseDto;
import reduck.reduck.domain.user.entity.DevelopAnnual;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.security.JwtProvider;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    UserRepository memberRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtRepository jwtRepository;
    @Test
    @Transactional
    void signUp() {
        User user = User.builder()
                .userId("test1")
                .password("1234")
                .name("nuhgnod")
                .email("1234@naver.com")
                .developAnnual(DevelopAnnual.eight)
                .company(null)
                .school(null)
                .profileImg(null)
                .build();
        memberRepository.save(user);
//
        User findUser = memberRepository.findByUserId("test1").orElseThrow(RuntimeException::new);
        Assertions.assertThat(user).isEqualTo(findUser);

    }

    @Test @Transactional
    void signIn() {
        signUp();
        User user1 = memberRepository.findByUserId("test1").orElseThrow(RuntimeException::new);

        String refreshToken1 = jwtProvider.createRefreshToken();
        RefreshToken refreshToken = RefreshToken.builder().refreshToken(refreshToken1).user(user1).build();
        RefreshToken save = jwtRepository.save(refreshToken);
        String token = jwtProvider.createToken(user1.getUserId(), user1.getRoles());

        SignInResponseDto.builder()
                .userId(user1.getUserId())
                .name(user1.getName())
                .email(user1.getEmail())
                .roles(user1.getRoles())
                .accessToken(token)
                .refreshToken(refreshToken1)
                .build();
        Assertions.assertThat(user1).isNotNull();
        Optional<RefreshToken> byId = jwtRepository.findById(save.getId());
        Assertions.assertThat(byId.get().getRefreshToken()).isEqualTo(refreshToken1);

    }

    @Test
    void getUser() {
    }
}