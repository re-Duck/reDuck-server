package reduck.reduck.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.board.dto.BoardDto;
import reduck.reduck.domain.board.entity.Board;
import reduck.reduck.domain.board.entity.BoardImage;
import reduck.reduck.domain.board.entity.mapper.BoardMapper;
import reduck.reduck.domain.board.repository.BoardImageRepository;
import reduck.reduck.domain.board.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;

import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.exception.CommonException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final BoardMapper boardMapper;
    private final String PATH = "C:\\reduckStorage\\board";

    public void createBoard(BoardDto boardDto, List<MultipartFile> multipartFileList) {
        System.out.println("boardDto = " + boardDto.getImagePathList());
        Board boardEntity = boardMapper.from(boardDto);
        Board board = boardRepository.save(boardEntity);
        saveImage(board, multipartFileList);
    }

    public void saveImage(Board board, List<MultipartFile> multipartFileList) {
        String abd = " ";
        multipartFileList.stream().forEach(multipartFile -> {
            String originalFilename = multipartFile.getOriginalFilename();
            String extension = originalFilename.split("\\.")[1];
            String storageFileName = UUID.randomUUID() + "." + extension;
            long size = multipartFile.getSize();
            Path imagePath = Paths.get(PATH, storageFileName);
            BoardImage boardImage = (BoardImage) BoardImage.builder()
                    .board(board)
                    .storageFileName(storageFileName)
                    .uploadeFiledName(originalFilename)
                    .path(String.valueOf(imagePath))
                    .extension(extension)
                    .size(size)
                    .build();
            try {
                Files.write(imagePath, multipartFile.getBytes());
                BoardImage save = boardImageRepository.save(boardImage);

//                return boardImageRepository.save(boardImage);

            } catch (Exception e) {
                log.error("이미지 저장 실패", e);
                throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
            }
        });
        Stream<String> stream =
                multipartFileList.stream().map(multipartFile -> save(multipartFile));


    }

    public String save(MultipartFile multipartFile) {
        return null;
    }
}