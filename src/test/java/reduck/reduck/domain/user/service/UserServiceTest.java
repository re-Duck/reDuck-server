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
import org.springframework.http.codec.multipart.Part;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
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
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Transactional
    @DisplayName("정상 회원가입")
    @ParameterizedTest
    @CsvSource("test1,p39pwt12!, donghun, zhfptm12@naver.com,3,naver,CNU")
    void 정상회원가입(String userId, String password, String name, String email, String developAnnual, String company, String school) throws Exception {
        System.out.println("==================================================================");
        System.out.println("userId = " + userId + ", password = " + password + ", name = " + name + ", email = " + email + ", developAnnual = " + developAnnual + ", company = " + company + ", school = " + school);
        System.out.println("==================================================================");
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
                .developAnnual(developAnnual)
                .company(company)
                .school(school)
                .build();
        User user = userService.signUp(signUpDto, file);
        String format1 = user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));//spring boot ms 9자리, mysql6자리
        String format2 = user.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));//spring boot ms 9자리, mysql6자리
        user.setCreatedAt(LocalDateTime.parse(format1, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
        user.setUpdatedAt(LocalDateTime.parse(format2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")));
        User byUserId = userService.findByUserId(user.getUserId());
        Assertions.assertThat(user).isEqualTo(byUserId);
    }

    @Transactional
    @DisplayName("회원가입 유효성 검사")
    @ParameterizedTest
    @MethodSource("provideUserObject")
    void 회원가입유효성검사(String userId, String password, String name, String email, String developAnnual, String company, String school, MockMultipartFile file,Exception exception) throws Exception {
        System.out.println("==================================================================");
        System.out.println("userId = " + userId + ", password = " + password + ", name = " + name + ", email = " + email + ", developAnnual = " + developAnnual + ", company = " + company + ", school = " + school);
        System.out.println("==================================================================");
        SignUpDto signUpDto = SignUpDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .developAnnual(developAnnual)
                .company(company)
                .school(school)
                .build();
        String path = "/user/" + userId;
        MockMultipartFile jsonPart = new MockMultipartFile("signUpDto", "signUpDto", "application/json", gson.toJson(signUpDto).getBytes(StandardCharsets.UTF_8));

        ResultActions resultActions = mockMvc.perform(multipart(path)
                        .file(file)
                        .file(jsonPart)
                )
                .andDo(System.out::println).andExpect(status().isBadRequest()).andExpect(
                        ex -> org.junit.jupiter.api.Assertions.assertTrue(ex.getResolvedException()
                                .getClass()
                                .isAssignableFrom(exception.getClass())));

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
                Arguments.of("test1", "p39pwt12!", "donghun", "zhfptm12@naver.com", "3", "naver", "CNU", file, DataIntegrityViolationException.class), //아이디 중복
                Arguments.of("test12", "p39pwt12~!", "donghun", "zhfptm12@naver.com", "3", "naver", "CNU", file, MethodArgumentNotValidException.class)
//                Arguments.of("test3","p39pwt12!","donghun", "zhfptm12@naver.com", "3","naver" ,"CNU"),
//                Arguments.of("test4","p39pwt12!","donghun", "zhfptm12@naver.com", "3","naver" ,"CNU")

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