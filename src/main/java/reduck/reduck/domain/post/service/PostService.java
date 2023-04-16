package reduck.reduck.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostImage;
import reduck.reduck.domain.post.entity.mapper.CommentResponseDtoMapper;
import reduck.reduck.domain.post.entity.mapper.PostMapper;
import reduck.reduck.domain.post.entity.mapper.PostResponseDtoMapper;
import reduck.reduck.domain.post.repository.PostImageRepository;
import reduck.reduck.domain.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;

import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.PostErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.CommonException;
import reduck.reduck.global.exception.exception.PostException;
import reduck.reduck.global.exception.exception.UserException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository boardRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;
    private final String PATH = "C:\\reduckStorage\\board";
    private static final String DEV_PATH = "/home/nuhgnod/develup/storage";

    public Post createPost(PostDto postDto, List<MultipartFile> multipartFiles) {
        String userId = postDto.getUserId();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
        Post postEntity = PostMapper.from(postDto);
        postEntity.setUser(user);
        Post post = boardRepository.save(postEntity);
        saveImages(post, multipartFiles);
        return post;
    }

    public void saveImages(Post post, List<MultipartFile> multipartFiles) {
        if (multipartFiles.size() == 1) {
            System.out.println("null = " + null);
            return;
        }
//        multipartFiles.stream().map(multipartFile -> saveImage(board, multipartFile)).forEach(path -> {
//            System.out.println("path = " + path);
//        });
//
        Stream<String> stream = multipartFiles.stream().map(multipartFile -> saveImage(post, multipartFile));
        stream.forEach(path -> {
            System.out.println("path = " + path);
        });

    }

    public String saveImage(Post post, MultipartFile multipartFile) {

        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.split("\\.")[1];
        String storageFileName = UUID.randomUUID() + "." + extension;
        long size = multipartFile.getSize();
        Path imagePath = Paths.get(PATH, storageFileName);
        PostImage postImage = (PostImage) PostImage.builder()
                .post(post)
                .storageFileName(storageFileName)
                .uploadeFiledName(originalFilename)
                .path(String.valueOf(imagePath))
                .extension(extension)
                .size(size)
                .build();
        try {
            Files.write(imagePath, multipartFile.getBytes());
            PostImage save = postImageRepository.save(postImage);
            return save.getPath();
        } catch (Exception e) {
            log.error("이미지 저장 실패", e);
            throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public PostResponseDto findByPostOriginId(String postOriginId) {
        Post post = boardRepository.findByPostOriginId(postOriginId).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));
//        List<CommentResponseDto> comments = new ArrayList<>();
//        for (Comment comm : post.getComments()) {
//            System.out.println("comm.toString() = " + comm.toString());
//            CommentResponseDto commentResponseDto = CommentResponseDtoMapper.of(post.getUser(), comm);
//            comments.add(commentResponseDto);
//        }
        PostResponseDto postResponseDto = PostResponseDtoMapper.from(post);

        return postResponseDto;
    }
    //paging의 경우.
    // DB에 post가 Long id순으로 삽임됨 => createdAt순

    // 단순 page만 있으면 최신순으로 page갯수만큼 조회
    public void findAllByPostTypeWithPage(String postType) {
    }
    // postOriginId 기준으로 page갯수 만큼 조회하는 경우.
    // postOriginId 게시글의 id값을 조회 후, -> select * from post where id > {postId} limit {page} 의 형식으로 구현.
    // => select * from post where ( select id from post where postOriginId = {postOriginId} ) > {postId} limit page
    public void findAllByPostTypeWithOriginIdAndPage(String postType) {
    }
}