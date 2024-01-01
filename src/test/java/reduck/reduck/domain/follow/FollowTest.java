package reduck.reduck.domain.follow;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
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
import reduck.reduck.domain.follow.dto.FollowRequest;
import reduck.reduck.domain.follow.service.FollowService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private String getAccessToken(String userId) {
        SignInDto dto = new SignInDto();
        dto.setPassword("p39pwt12!");
        dto.setUserId(userId == null ? "test1" : userId);
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
        String accessToken = getAccessToken("test1");
        FollowRequest dto = new FollowRequest();
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

    @Transactional
    @Test
    @DisplayName("팔로워 목록 조회")
    void getFollowers() {
        following();
        String accessToken = getAccessToken("test3");
        try {
            mockMvc.perform(get("/follow/followers")
                            .header("Authorization", accessToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("data[0].userId", Matchers.is("test1")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Test
    @DisplayName("팔로잉 목록 조회")
    void getFollowings() {
        following();
        String accessToken = getAccessToken("test1");
        try {
            mockMvc.perform(get("/follow/followings")
                            .header("Authorization", accessToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("data[0].userId", Matchers.is("test3")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
