//package reduck.reduck.domain.post.service;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//import reduck.reduck.domain.post.dto.PostDto;
//import reduck.reduck.domain.post.entity.Post;
//import reduck.reduck.domain.post.entity.PostType;
//import reduck.reduck.domain.post.repository.PostRepository;
//import reduck.reduck.domain.user.repository.UserRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//
//@SpringBootTest
//@ActiveProfiles("test")
//class PostServiceTest {
//    @Autowired
//    PostService boardService;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    PostRepository boardRepository;
//
//    @Test
//    @Transactional
//    void createBoard() {
//        PostDto postDto = new PostDto();
//        postDto.setPostType(PostType.qna);
//        postDto.setContent("qwe rty uio");
//        postDto.setTitle("test");
//        postDto.setUserId("test2");
//        postDto.setPostOriginId("origin");
//        List<MultipartFile> files = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            MockMultipartFile file
//                    = new MockMultipartFile(
//                    "file" + String.valueOf(i),
//                    "hello.txt",
//                    MediaType.TEXT_PLAIN_VALUE,
//                    "Hello, World!".getBytes()
//            );
//            files.add(file);
//        }
//        boardService.createPost(postDto, files);
//        Optional<Post> boardById = boardRepository.findById(1L);
////        Assertions.assertThat()
//    }
//
//    @Test
//    @Transactional
//    void saveImages(Post post, List<MultipartFile> files) {
//        boardService.saveImages(post, files);
//    }
//
//    @Test
//    @Transactional
//    String saveImage(MultipartFile multipartFile) {
//        return multipartFile.getName();
//    }
//}