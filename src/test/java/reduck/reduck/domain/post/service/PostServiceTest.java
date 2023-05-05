package reduck.reduck.domain.post.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.post.repository.PostRepository;
import reduck.reduck.domain.user.entity.Authority;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.security.JwtProvider;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class PostServiceTest {
    @Autowired
    PostService boardService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository boardRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private Gson gson;

    @Transactional
    @DisplayName("게시글 작성")
    @ParameterizedTest(name = "{index}:{0}")
    @MethodSource("providePostObject")
    void createBoard(String testName, MockMultipartFile file, PostDto postDto) {

        String path = "/post";
        Optional<User> test1 = userRepository.findByUserId(postDto.getUserId());
        List<Authority> objects = new ArrayList<>();
        objects.add(Authority.builder().id(10L).name("ROLE_USER").user(test1.get()).build());
        String accToken = "Bearer " + jwtProvider.createToken("test1", objects);
        MockMultipartFile jsonPart = new MockMultipartFile("postDto", "postDto", "application/json", gson.toJson(postDto).getBytes(StandardCharsets.UTF_8));
        try {
            mockMvc.perform(multipart(path)
                            .file(jsonPart)
                            .file(file)
                            .header("Authorization", accToken))
                    .andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Stream<Arguments> providePostObject() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        MockMultipartFile emptyFile = new MockMultipartFile("file", (byte[]) null);
        PostDto postDto = PostDto.builder()
                .userId("test1")
                .postType(PostType.valueOf("stack"))
                .postOriginId("post1")
                .postType(PostType.qna)
                .title("test title")
                .build();
        return Stream.of(
                Arguments.of("이미지 포함된 게시글 작성", file, postDto),
                Arguments.of("이미지 없는 게시글 작성", emptyFile, postDto)

        );
    }

}