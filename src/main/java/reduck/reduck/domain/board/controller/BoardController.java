package reduck.reduck.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.board.dto.BoardDto;
import reduck.reduck.domain.board.service.BoardService;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/board/{originId}")
    public ResponseEntity<Void> createBoard(HttpServletRequest request, @RequestPart BoardDto boardDto, @RequestPart(required = false) List<MultipartFile> multipartFiles) {
        boardService.createBoard(boardDto, multipartFiles);
        return ResponseEntity.ok().build();
    }

}
