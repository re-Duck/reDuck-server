package reduck.reduck.domain.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import reduck.reduck.domain.auth.repository.AuthRepository;
import reduck.reduck.domain.auth.service.AuthService;
import reduck.reduck.domain.user.dto.SignInDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.exception.UserException;
import reduck.reduck.global.security.JwtProvider;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    private WebApplicationContext webApplicationContext;

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
                .build();
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

//        MockMvc mockMvc
//                = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        mockMvc.perform(multipart("/user/test1").file(file))
//                .andExpect(status().isOk());
        userService.signUp(signUpDto, file);
        System.out.println("=====================================================================");
        //이미 존재하는 아이디로 회원가입.
        SignUpDto signUpDto2 = SignUpDto.builder()
                .userId("test2")
                .password("1234")
                .name("nuhgnod")
                .email("1234@naver.com")
                .developAnnual("8")
                .company(null)
                .school(null)
                .build();
        Assertions.assertThatThrownBy(() -> {
            userService.signUp(signUpDto2, file);
        }).isInstanceOf(Exception.class);
    }

    @Test @Transactional
    void signIn() throws Exception {
        try {
            //정상 작동
            SignInDto signInDto = new SignInDto();
            signInDto.setUserId("test1");
            signInDto.setPassword("1234");
            authService.signIn(signInDto);

            //아이디 일치 에러
            SignInDto signInDto2 = new SignInDto();
            signInDto2.setUserId("test2");
            signInDto2.setPassword("1234");
            Assertions.assertThatThrownBy(() -> {
                authService.signIn(signInDto2);
            }).isInstanceOf(UserException.class);

            //비밀번호 일치 에러
            SignInDto signInDto3 = new SignInDto();
            signInDto3.setUserId("test1");
            signInDto3.setPassword("test1@@@");
            Assertions.assertThatThrownBy(() -> {
                authService.signIn(signInDto3);
            }).isInstanceOf(UserException.class);
        } catch (UserException e) {
            Assertions.assertThat(e.getClass().toString()).isEqualTo("class org.springframework.security.authentication.BadCredentialsException");

        }


    }

    @Test
    @Transactional
    void getUser() throws Exception {
        User user = userService.findByUserId("test1");
        Optional<User> findUser = userRepository.findByUserId("test1");
        Assertions.assertThat(user).isEqualTo(findUser.get());

    }
}