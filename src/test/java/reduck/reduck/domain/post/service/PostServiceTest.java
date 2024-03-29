package reduck.reduck.domain.post.service;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
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
import reduck.reduck.domain.auth.dto.SignInDto;
import reduck.reduck.domain.auth.dto.SignInResponseDto;
import reduck.reduck.domain.auth.service.AuthService;
import reduck.reduck.domain.post.dto.CommentDto;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.dto.UpdateCommentDto;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.post.repository.PostRepository;
import reduck.reduck.domain.user.entity.Authority;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.security.JwtProvider;
import reduck.reduck.util.AuthenticationToken;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class PostServiceTest {
    @Autowired
    PostService boardService;
    @Autowired
    AuthService authService;
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
    void 게시글작성(String testName, PostDto postDto) throws Exception {
        String accessToken = getAccessToken();
        String path = "/post";
        String s = gson.toJson(postDto);
        mockMvc.perform(post(path)
                        .content(s)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void 이미지저장() throws Exception {
        String accessToken = getAccessToken();
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        String path = "/post/image";
        mockMvc.perform(multipart(path)
                        .file(file)
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void 게시글1개조회() throws Exception {
        String path = "/post/detail/post123123";
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("postContent", Matchers.is("<p>hello</p>")));
    }

    @Transactional
    @ParameterizedTest(name = "{index}:{0}")
    @MethodSource("providePostOriginId")
    void 게시글_여러개_조회(String testName, String id) throws Exception {
        String path = "/post?postType=qna&page=3&postOriginId=" + id;
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postOriginId", Matchers.is(id == "" ? "post22" : "post1")))
                .andExpect(jsonPath("$[1].postOriginId", Matchers.is(id == "" ? "post11" : "post55")))
                .andExpect(jsonPath("$[2].postOriginId", Matchers.is(id == "" ? "post2" : "post123123")));

    }

    @Test
    @Transactional
    void 게시글_삭제() throws Exception {
        String path = "/post/post55";
        String accessToken = getAccessToken();

        mockMvc.perform(delete(path)
                        .header("Authorization", accessToken))
                .andExpect(status().isNoContent());

    }

    @Test
    @Transactional
    void 게시글_수정() throws Exception {
        String path = "/post/post55";
        String accessToken = getAccessToken();
        PostDto update = PostDto.builder()
                .content("<p>updated@@@@@@@@@@@@@</p>")
                .postOriginId("post55")
                .postType(PostType.stack)
                .title("test title")
                .build();
        String s = gson.toJson(update);
        mockMvc.perform(put(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void 댓글_작성() throws Exception {
        String path = "/post/comment";
        String accessToken = getAccessToken();
        CommentDto comment = new CommentDto();
        comment.setCommentOriginId("comment3");
        comment.setContent("댓글이다.");
        comment.setPostOriginId("post123123");
        String s = gson.toJson(comment);
        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s)
                        .header("Authorization", accessToken))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void 댓글_삭제() throws Exception {
        String path = "/post/comment/comment1";
        String accessToken = getAccessToken();

        mockMvc.perform(delete(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken))
                .andExpect(status().isNoContent());
    }
    @Test
    @Transactional
    void 댓글_수정() throws Exception {
        String path = "/post/comment/comment1";
        String accessToken = getAccessToken();
        UpdateCommentDto comment = new UpdateCommentDto();
        comment.setCommentOriginId("comment1");
        comment.setContent("댓글수정이다.");
        String s = gson.toJson(comment);
        mockMvc.perform(put(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s)
                        .header("Authorization", accessToken))
                .andExpect(status().isOk());
    }
    private static Stream<Arguments> providePostObject() {
        PostDto postDto = PostDto.builder()
                .content("<p>hello</p>")
                .postOriginId("post111")
                .postType(PostType.qna)
                .title("test title")
                .build();
        PostDto empty = PostDto.builder()
                .content("<p>hello</p>")
                .postOriginId("post222")
                .postType(PostType.qna)
                .title("test title")
                .build();
        return Stream.of(
                Arguments.of("content 있는 게시글 작성", postDto),
                Arguments.of("content 없는 게시글 작성", empty)

        );
    }

    private static Stream<Arguments> providePostOriginId() {
        String main = "";
        String scroll = "post2";
        return Stream.of(
                Arguments.of("메인 페이지 조회", main),
                Arguments.of("스크롤 조회", scroll)
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
}