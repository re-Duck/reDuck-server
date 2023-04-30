package reduck.reduck.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostImage;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.post.entity.mapper.PostMapper;
import reduck.reduck.domain.post.dto.mapper.PostResponseDtoMapper;
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
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;
    private final String PATH = "C:\\reduckStorage\\board";
    private static final String DEV_PATH = "/home/nuhgnod/develup/storage";

    @Transactional
    public Post createPost(PostDto postDto, List<MultipartFile> multipartFiles) {
        String userId = postDto.getUserId();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
        Post postEntity = PostMapper.from(postDto);
        postEntity.setUser(user);
        Post post = postRepository.save(postEntity);
        saveImages(post, multipartFiles);
        return post;
    }

    public void saveImages(Post post, List<MultipartFile> multipartFiles) {
        if (multipartFiles.get(0).isEmpty()) {
            return;
        }
        Stream<String> stream = multipartFiles
                .stream()
                .map(multipartFile -> saveImage(post, multipartFile));
    }

    @Transactional
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

    @Transactional
    public PostResponseDto findByPostOriginId(String postOriginId) {
        Post post = postRepository.findByPostOriginId(postOriginId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));
        PostResponseDto postResponseDto = PostResponseDtoMapper.from(post);

        return postResponseDto;
    }

    // paging의 경우.
    // DB에 post가 Long id순으로 삽임됨 == createdAt순
    // 단순 page만 있으면 최신순으로 page갯수만큼 조회
    public List<PostResponseDto> findPostAllByPostTypeWithPage(String postType, int page) {
        Pageable pageable = PageRequest.of(0, page);
        List<Post> posts = postRepository.findAllByPostTypeOrderByIdDescLimitPage(PostType.getType(postType), pageable);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        posts.forEach(post -> {
            postResponseDtos.add(PostResponseDtoMapper.excludeCommentsFrom(post));
        });
        return postResponseDtos;
    }

    // postOriginId 기준으로 page갯수 만큼 조회하는 경우.
    // postOriginId 게시글의 id값을 조회 후, -> select * from post where id > {postId} limit {page} 의 형식으로 구현.
    // => select * from post where ( select id from post where postOriginId = {postOriginId} ) > {postId} limit page
    public List<PostResponseDto> findAllByPostTypeAndPostOriginIdOrderByIdDescLimitPage(String postType, String postOriginId, int page) {
        Pageable pageable = PageRequest.of(0, page);

        List<Post> posts = postRepository.findAllByPostTypeAndPostOriginIdOrderByIdDescLimitPage(PostType.getType(postType), postOriginId, pageable);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        posts.forEach(post -> {
            postResponseDtos.add(PostResponseDtoMapper.excludeCommentsFrom(post));

        });
        return postResponseDtos;

    }
}