package reduck.reduck.domain.auth.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import reduck.reduck.domain.auth.repository.AuthRepository;
import reduck.reduck.domain.user.repository.UserRepository;

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

}