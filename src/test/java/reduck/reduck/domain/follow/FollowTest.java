package reduck.reduck.domain.follow;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.auth.dto.SignInDto;
import reduck.reduck.domain.auth.dto.SignInResponseDto;
import reduck.reduck.domain.auth.service.AuthService;
import reduck.reduck.domain.follow.dto.FollowDto;
import reduck.reduck.domain.follow.service.FollowService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class FollowTest {
    @Autowired
    FollowService followService;
    @Autowired
    AuthService authService;
    @Autowired
    private Gson gson;

    @Autowired
    private MockMvc mockMvc;

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

    @Transactional
    @Test
    @DisplayName("팔로잉 기능")
    void following() {
        String accessToken = getAccessToken();
        FollowDto dto = new FollowDto();
        dto.setUserId("test3");
        String json = gson.toJson(dto);
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/follow")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", accessToken)
                    )
                    .andExpect(status().isCreated());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
