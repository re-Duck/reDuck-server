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
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JwtRepository jwtRepository;
    @Autowired
    UserService userService;

    @Test
    @Transactional
    void signUp() throws Exception {
        //정상작동.
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
       userService.signUp(signUpDto);
        //이미 존재하는 아이디로 회원가입.

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
        Assertions.assertThatThrownBy(() -> {
            userService.signUp(signUpDto2);
        }).isInstanceOf(Exception.class);
    }

    @Test @Transactional
    void signIn() throws Exception {
        try {
            //정상 작동
            SignInDto signInDto = new SignInDto();
            signInDto.setUserId("test1");
            signInDto.setPassword("1234");
            userService.signIn(signInDto);

            //아이디 일치 에러
            SignInDto signInDto2 = new SignInDto();
            signInDto2.setUserId("test2");
            signInDto2.setPassword("1234");
            Assertions.assertThatThrownBy(() -> {
                userService.signIn(signInDto2);
            }).isInstanceOf(BadCredentialsException.class);

            //비밀번호 일치 에러
            SignInDto signInDto3 = new SignInDto();
            signInDto3.setUserId("test1");
            signInDto3.setPassword("test1@@@");
            Assertions.assertThatThrownBy(() -> {
                userService.signIn(signInDto3);
            }).isInstanceOf(BadCredentialsException.class);
        } catch (BadCredentialsException e) {
            Assertions.assertThat(e.getClass().toString()).isEqualTo("class org.springframework.security.authentication.BadCredentialsException");

        }


    }

    @Test
    @Transactional
    void getUser() throws Exception {
        User user = userService.getUser("test1");
        Optional<User> findUser = userRepository.findByUserId("test1");
        Assertions.assertThat(user).isEqualTo(findUser.get());

    }
}