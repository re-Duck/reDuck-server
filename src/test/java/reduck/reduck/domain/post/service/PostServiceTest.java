package reduck.reduck.domain.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.board.dto.BoardDto;
import reduck.reduck.domain.board.entity.Board;
import reduck.reduck.domain.board.entity.BoardType;
import reduck.reduck.domain.board.repository.BoardRepository;
import reduck.reduck.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@ActiveProfiles("test")
class BoardServiceTest {
    @Autowired
    BoardService boardService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;

    @Test
    @Transactional
    void createBoard() {
        BoardDto boardDto = new BoardDto();
        boardDto.setBoardType(BoardType.qna);
        boardDto.setContent("qwe rty uio");
        boardDto.setTitle("test");
        boardDto.setUserId("test2");
        boardDto.setPostOriginId("origin");
        List<MultipartFile> files = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MockMultipartFile file
                    = new MockMultipartFile(
                    "file" + String.valueOf(i),
                    "hello.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    "Hello, World!".getBytes()
            );
            files.add(file);
        }
        boardService.createBoard(boardDto, files);
        Optional<Board> boardById = boardRepository.findById(1L);
//        Assertions.assertThat()
    }

    @Test
    @Transactional
    void saveImages(Board board, List<MultipartFile> files) {
        boardService.saveImages(board, files);
    }

    @Test
    @Transactional
    String saveImage(MultipartFile multipartFile) {
        return multipartFile.getName();
    }
}