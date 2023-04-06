package reduck.reduck.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.board.dto.BoardDto;
import reduck.reduck.domain.board.entity.Board;
import reduck.reduck.domain.board.repository.BoardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public void createBoard(BoardDto boardDto, List<MultipartFile> multipartFileList) {
        System.out.println("boardDto = " + boardDto.getImagePathList());
        Board board = Board.builder()
                .content(boardDto.getContent())
                .title(boardDto.getTitle())
                .originId(boardDto.getOriginId())
                .build();
//        boardRepository.save(board);
    }
}
