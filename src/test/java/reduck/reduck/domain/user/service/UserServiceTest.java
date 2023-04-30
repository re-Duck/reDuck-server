package reduck.reduck.domain.user.service;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import reduck.reduck.domain.auth.repository.AuthRepository;
import reduck.reduck.domain.auth.service.AuthService;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.security.JwtProvider;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
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
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Transactional
    @DisplayName("정상 회원가입")
    @ParameterizedTest
    @CsvSource("test2, p39pwt12!, donghun, zhfptm12@naver.com,2022,naver,CNU")
    void 정상회원가입(String userId, String password, String name, String email, int developYear, String company, String school) throws Exception {

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        SignUpDto signUpDto = SignUpDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .developYear(developYear)
                .company(company)
                .school(school)
                .build();
        User user = userService.signUp(signUpDto, file);
        User byUserId = userService.findByUserId(user.getUserId());
        Assertions.assertThat(user.getUserId()).isEqualTo(byUserId.getUserId());
    }

    @Transactional
    @DisplayName("회원가입 유효성 검사")
    @ParameterizedTest(name = "{index}:{0}")
    @MethodSource("provideUserObject")
    void 회원가입유효성검사(String testName, String userId, String password, String name, String email, int developYear, String company, String school, MockMultipartFile file, Class obj) throws Exception {
        SignUpDto signUpDto = SignUpDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .developYear(developYear)
                .company(company)
                .school(school)
                .build();

        String path = "/user/" + userId;
        MockMultipartFile jsonPart = new MockMultipartFile("signUpDto", "signUpDto", "application/json", gson.toJson(signUpDto).getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(multipart(path)
//                        .file(file)
                        .file(jsonPart))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> org.junit.jupiter.api.Assertions.assertTrue(ex.getResolvedException()
                        .getClass().isAssignableFrom(obj)));
    }

    private static Stream<Arguments> provideUserObject() {
        MockMultipartFile file
                = new MockMultipartFile(
                "multipartFile",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        return Stream.of(
                Arguments.of("아이디 중복", "test1", "p39pwt12!", "donghun", "zhfptm12@naver.com", 0, "naver", "CNU", file, DataIntegrityViolationException.class),
                Arguments.of("비밀번호 패턴 불일치", "test12", "p39pwt12~!", "donghun", "zhfptm12@naver.com", 0, "naver", "CNU", file, MethodArgumentNotValidException.class), //
                Arguments.of("비밀번호 8자 안됨 (6자)", "test12", "p39pwt", "donghun", "zhfptm12@naver.com", 0, "naver", "CNU", file, MethodArgumentNotValidException.class), //
                Arguments.of("비밀번호 15자 이상 (25자)", "test12", "p39pwt12p39pwt12p39pwt12!", "donghun", "zhfptm12@naver.com", 0, "naver", "CNU", file, MethodArgumentNotValidException.class), //
                Arguments.of("비밀번호 숫자 없음", "test12", "abcdefghi!", "donghun", "zhfptm12@naver.com", 0, "naver", "CNU", file, MethodArgumentNotValidException.class), //
                Arguments.of("비밀번호 영문자 없음", "test12", "12345678!", "donghun", "zhfptm12@naver.com", 0, "naver", "CNU", file, MethodArgumentNotValidException.class), //
                Arguments.of("비밀번호 특수문자 없음", "test4", "p39pwt12qwe", "donghun", "zhfptm12@naver.com", 0, "naver", "CNU", file, MethodArgumentNotValidException.class), //
                Arguments.of("이름 누락", "test4", "p39pwt12!", "", "zhfptm12@naver.com", 0, "naver", "CNU", file, MethodArgumentNotValidException.class), //
                Arguments.of("메일 누락", "test4", "p39pwt12!", "donghun", "", 0, "naver", "CNU", file, MethodArgumentNotValidException.class)


        );
    }
//
//    @Test @Transactional
//    void signIn() throws Exception {
//        try {
//            //정상 작동
//            SignInDto signInDto = new SignInDto();
//            signInDto.setUserId("test1");
//            signInDto.setPassword("1234");
//            authService.signIn(signInDto);
//
//            //아이디 일치 에러
//            SignInDto signInDto2 = new SignInDto();
//            signInDto2.setUserId("test2");
//            signInDto2.setPassword("1234");
//            Assertions.assertThatThrownBy(() -> {
//                authService.signIn(signInDto2);
//            }).isInstanceOf(UserException.class);
//
//            //비밀번호 일치 에러
//            SignInDto signInDto3 = new SignInDto();
//            signInDto3.setUserId("test1");
//            signInDto3.setPassword("test1@@@");
//            Assertions.assertThatThrownBy(() -> {
//                authService.signIn(signInDto3);
//            }).isInstanceOf(UserException.class);
//        } catch (UserException e) {
//            Assertions.assertThat(e.getClass().toString()).isEqualTo("class org.springframework.security.authentication.BadCredentialsException");
//
//        }
//
//
//    }
//
//    @Test
//    @Transactional
//    void getUser() throws Exception {
//        User user = userService.findByUserId("test1");
//        Optional<User> findUser = userRepository.findByUserId("test1");
//        Assertions.assertThat(user).isEqualTo(findUser.get());
//
//    }
}