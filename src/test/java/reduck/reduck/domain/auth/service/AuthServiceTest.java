package reduck.reduck.domain.auth.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reduck.reduck.domain.auth.dto.SignInDto;
import reduck.reduck.domain.auth.repository.AuthRepository;
import reduck.reduck.domain.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AuthServiceTest {

    @Autowired
    AuthService authService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    @Transactional
    @DisplayName("정상 회원가입")
    @ParameterizedTest
    @CsvSource("test2, p39pwt12!, donghun, zhfptm12@naver.com,2022,naver,CNU")
    void signIn() throws Exception {

        SignInDto dto = new SignInDto();
        dto.setPassword("p39pwt12!");
        dto.setUserId("test1");
        String s = gson.toJson(dto);
        mockMvc.perform(post("/login")
                        .content(s)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

    }
}