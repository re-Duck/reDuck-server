package reduck.reduck.domain.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.jwt.entity.RefreshToken;
import reduck.reduck.domain.jwt.repository.JwtRepository;
import reduck.reduck.domain.user.dto.SignInDto;
import reduck.reduck.domain.user.dto.SignInResponseDto;
import reduck.reduck.domain.user.dto.SignUpDto;
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
    @Autowired
    UserService userService;
    @Test
    @Transactional
    void signUp() throws Exception {
        SignUpDto signUpDto = SignUpDto.builder()
                .userId("test2")
                .password("1234")
                .name("nuhgnod")
                .email("1234@naver.com")
                .developAnnual("8")
                .company(null)
                .school(null)
                .profileImg(null)
                .build();
        boolean b = userService.signUp(signUpDto);
        Assertions.assertThat(b).isEqualTo(true);
        boolean b2;
        SignUpDto signUpDto2 = SignUpDto.builder()
                .userId("test2")
                .password("1234")
                .name("nuhgnod")
                .email("1234@naver.com")
                .developAnnual("8")
                .company(null)
                .school(null)
                .profileImg(null)
                .build();
        try {
             b2 = userService.signUp(signUpDto2);
            Assertions.assertThat(b2).isEqualTo(false);
            System.out.println("b2 = " + b2);
        }catch (Exception e){

        }


    }

    @Test @Transactional
    void signIn() throws Exception {
        try {

            SignInDto signInDto = SignInDto.builder()
                    .userId("test1")
                    .password("1234")
                    .build();
            SignInResponseDto signInResponseDto = userService.signIn(signInDto);

            SignInDto signInDto2 = SignInDto.builder()
                    .userId("test3")
                    .password("1234")
                    .build();
            SignInResponseDto signInResponseDto2 = userService.signIn(signInDto2);

            SignInDto signInDto3 = SignInDto.builder()
                    .userId("test1")
                    .password("1234@@@@@@@@")
                    .build();
            SignInResponseDto signInResponseDto3 = userService.signIn(signInDto3);
        } catch (BadCredentialsException e) {
            Assertions.assertThat(e.getClass().toString()).isEqualTo("class org.springframework.security.authentication.BadCredentialsException");

        }


    }

    @Test
    void getUser() {
    }
}