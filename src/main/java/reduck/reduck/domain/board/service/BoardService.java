package reduck.reduck.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.board.dto.BoardDto;
import reduck.reduck.domain.board.entity.Board;
import reduck.reduck.domain.board.entity.BoardImage;
import reduck.reduck.domain.board.entity.BoardType;
import reduck.reduck.domain.board.entity.mapper.BoardMapper;
import reduck.reduck.domain.board.repository.BoardImageRepository;
import reduck.reduck.domain.board.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;

import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.CommonException;
import reduck.reduck.global.exception.exception.UserException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardImageRepository boardImageRepository;
    private final String PATH = "C:\\reduckStorage\\board";
    private static final String DEV_PATH = "/home/nuhgnod/develup/storage";

    public Board createBoard(BoardDto boardDto, List<MultipartFile> multipartFiles) {
        String userId = boardDto.getUserId();

        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
        Board boardEntity = BoardMapper.from(boardDto);
        boardEntity.setUser(user);

        Board board = boardRepository.save(boardEntity);
        saveImages(board, multipartFiles);
        return board;
    }

    public void saveImages(Board board, List<MultipartFile> multipartFiles) {
        System.out.println("multipartFiles = " + multipartFiles.size());
        System.out.println("multipartFiles = " + multipartFiles.get(0));
        System.out.println("multipartFiles = " + multipartFiles.get(0).getOriginalFilename());
        System.out.println("multipartFiles = " + multipartFiles.get(0).getResource());
        if (multipartFiles.isEmpty()) {
            System.out.println("null = " + null);
            return;
        }
//        multipartFiles.stream().map(multipartFile -> saveImage(board, multipartFile)).forEach(path -> {
//            System.out.println("path = " + path);
//        });
//
        Stream<String> stream = multipartFiles.stream().map(multipartFile -> saveImage(board, multipartFile));
        stream.forEach(path -> {
            System.out.println("path = " + path);
        });

    }

    public String saveImage(Board board, MultipartFile multipartFile) {

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
            return save.getPath();
        } catch (Exception e) {
            log.error("이미지 저장 실패", e);
            throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}