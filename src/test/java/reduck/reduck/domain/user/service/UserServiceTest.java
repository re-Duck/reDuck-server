package reduck.reduck.domain.user.service;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import reduck.reduck.domain.auth.dto.SignInDto;
import reduck.reduck.domain.auth.dto.SignInResponseDto;
import reduck.reduck.domain.auth.entity.EmailType;
import reduck.reduck.domain.auth.repository.AuthRepository;
import reduck.reduck.domain.auth.service.AuthService;
import reduck.reduck.domain.user.dto.ModifyUserDto;
import reduck.reduck.domain.user.dto.SignUpDto;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.exception.AuthException;
import reduck.reduck.global.security.JwtProvider;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
    @DisplayName("회원 탈퇴")
    @Test
    void 회원탈퇴() throws Exception {
        String accessToken = getAccessToken();
        mockMvc.perform(delete("/user").header("Authorization", accessToken))
                .andExpect(status().isNoContent());
    }

    @Transactional
    @DisplayName("유저 정보 확인")
    @Test
    void 유저정보() throws Exception {
        mockMvc.perform(get("/user/test1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is("donghun")))
                .andExpect(jsonPath("company", is("naver")))
                .andReturn();
    }

    @Transactional
    @DisplayName("유저 정보 변경")
    @Test
    void 유저정보변경() throws Exception {
        String accessToken = getAccessToken();
        String schoolEmailToken = createEmailToken("zhfptm12@o.cnu.ac.kr", EmailType.school, 111111);
        String companyToken = createEmailToken("zhfptm12@naver.com", EmailType.company, 111111);
        String userToken = createEmailToken("zhfptm12@gmail.com", EmailType.user, 111111);

        ModifyUserDto build = ModifyUserDto.builder()
                .name("new name")
                .email("zhfptm12@gmail.com")
                .emailAuthToken(userToken)
                .company("no")
                .companyEmail("zhfptm12@naver.com")
                .companyEmailAuthToken(companyToken)
                .school("cnu")
                .schoolEmail("zhfptm12@o.cnu.ac.kr")
                .schoolEmailAuthToken(schoolEmailToken)
                .developYear(2018)
                .build();
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        String path = "/user/test1";
        MockMultipartFile jsonPart = new MockMultipartFile("modifyUserDto", "modifyUserDto", "application/json", gson.toJson(build).getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(multipart(HttpMethod.PUT, path)
                        .file(file)
                        .file(jsonPart)
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isCreated())
        ;

    }

    @Transactional
    @DisplayName("유저 정보 변경 예외테스트")
    @ParameterizedTest(name = "{index}:{0}")
    @MethodSource("provideModifyDtoObject")
    void 유저정보변경예외테스트(String testName,String name, String email, String company, String companyEmail, String school, String schoolEmail
            , int developYear, MockMultipartFile file, Class obj) throws Exception {
        String accessToken = getAccessToken();

        String schoolEmailToken =  createEmailToken("zhfptm12not@o.cnu.ac.kr", EmailType.school, 111111);
        String companyToken = createEmailToken("zhfptm12not@naver.com", EmailType.company, 111111);
        String userToken = createEmailToken("zhfptm12not@naver.com", EmailType.user, 111111);

        ModifyUserDto build = ModifyUserDto.builder()
                .name(name)
                .email(email)
                .emailAuthToken(userToken)
                .company(company)
                .companyEmail(companyEmail)
                .companyEmailAuthToken(companyToken)
                .school(school)
                .schoolEmail(schoolEmail)
                .schoolEmailAuthToken(schoolEmailToken)
                .developYear(developYear)
                .build();

        String path = "/user/test1";
        MockMultipartFile jsonPart = new MockMultipartFile("modifyUserDto", "modifyUserDto", "application/json", gson.toJson(build).getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(multipart(HttpMethod.PUT, path)
                        .file(file)
                        .file(jsonPart)
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isBadRequest())
                .andExpect(ex -> org.junit.jupiter.api.Assertions.assertTrue(ex.getResolvedException()
                        .getClass().isAssignableFrom(obj)));
        ;

    }
    private static  Stream<Arguments> provideModifyDtoObject() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        return Stream.of(
                Arguments.of("user email token 유효성", "donghun", "zhfptm12@gmail.com"
                        , "naver", "zhfptm12@naver.com", "CNU","zhfptm12@o.cnu.ac.kr"
                        ,2020,file, AuthException.class)
        );
    }
    @Transactional
    @DisplayName("아이디 중복 확인")
    @ParameterizedTest
    @CsvSource("test2")
    void 아이디중복확인(String userId) throws Exception {
        mockMvc.perform(get("/user/duplicate/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

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
        String emailToken = createEmailToken(email, EmailType.user, 111111);
        SignUpDto signUpDto = SignUpDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .emailAuthToken(emailToken)
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
        String emailToken = createEmailToken(email, EmailType.user, 111111);

        SignUpDto signUpDto = SignUpDto.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .emailAuthToken(emailToken)
                .developYear(developYear)
                .company(company)
                .school(school)
                .build();

        String path = "/user";
        MockMultipartFile jsonPart = new MockMultipartFile("signUpDto", "signUpDto", "application/json", gson.toJson(signUpDto).getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(multipart(path)
                        .file(file)
                        .file(jsonPart))
                .andExpect(status().isBadRequest())
                .andExpect(ex -> org.junit.jupiter.api.Assertions.assertTrue(ex.getResolvedException()
                        .getClass().isAssignableFrom(obj)));
    }

    private static Stream<Arguments> provideUserObject() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
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

    private String getAccessToken() {
        SignInDto dto = new SignInDto();
        dto.setPassword("p39pwt12!");
        dto.setUserId("test1");
        //로그인 먼저
        SignInResponseDto signInResponseDto = authService
                .signIn(dto);
        String accessToken = "Bearer " + signInResponseDto.getAccessToken();
        return accessToken;
    }
    private String createEmailToken(String email, EmailType type, int num) {
        return jwtProvider.createEmailToken(email, type, num);
    }
}