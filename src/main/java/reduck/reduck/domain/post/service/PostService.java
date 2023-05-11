package reduck.reduck.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostType;
import reduck.reduck.domain.post.entity.mapper.PostMapper;
import reduck.reduck.domain.post.dto.mapper.PostResponseDtoMapper;
import reduck.reduck.domain.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.AuthErrorCode;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.PostErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.AuthException;
import reduck.reduck.global.exception.exception.CommonException;
import reduck.reduck.global.exception.exception.PostException;
import reduck.reduck.global.exception.exception.UserException;
import reduck.reduck.util.AuthenticationToken;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final String PATH = "C:\\reduckStorage\\post";
    private static final String DEV_PATH = "/home/nuhgnod/develup/storage/post";

    @Transactional
    public void createPost(PostDto postDto) {
        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
        Post postEntity = PostMapper.from(postDto);
        postEntity.setUser(user);
        postRepository.save(postEntity);
    }

    @Transactional
    public String saveMultipartFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.split("\\.")[1];
        String storageFileName = UUID.randomUUID() + "." + extension;
        String path = PATH + "/" + AuthenticationToken.getUserId(); //폴더 경로
        File Folder = new File(path);
        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try{
                Folder.mkdir(); //폴더 생성합니다.
                System.out.println("폴더가 생성되었습니다.");
            }
            catch(Exception e){
                e.getStackTrace();
            }
        }else {
            System.out.println("이미 폴더가 생성되어 있습니다.");
        }

        Path imagePath = Paths.get(path, storageFileName); //local용

        try {
            Files.write(imagePath, multipartFile.getBytes());
            return String.valueOf(imagePath);
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
    public List<PostResponseDto> findPostAllByPostTypeWithPage(List<String> types, int page) {
        List<PostType> postTypes = types.stream().map(type -> PostType.getType(type)).collect(Collectors.toList());
        Pageable pageable = PageRequest.of(0, page);
        List<Post> posts = postRepository.findAllByPostTypeOrderByIdDescLimitPage(postTypes, pageable).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));
        List<PostResponseDto> postResponseDtos = posts
                .stream()
                .map(post -> PostResponseDtoMapper.excludeCommentsFrom(post))
                .collect(Collectors.toList());
        return postResponseDtos;
    }

    // postOriginId 기준으로 page갯수 만큼 조회하는 경우.
    // postOriginId 게시글의 id값을 조회 후, -> select * from post where id > {postId} limit {page} 의 형식으로 구현.
    // => select * from post where ( select id from post where postOriginId = {postOriginId} ) > {postId} limit page
    public List<PostResponseDto> findAllByPostTypeAndPostOriginIdOrderByIdDescLimitPage(String postType, String postOriginId, int page) {
        Pageable pageable = PageRequest.of(0, page);

        List<Post> posts = postRepository.findAllByPostTypeAndPostOriginIdOrderByIdDescLimitPage(PostType.getType(postType), postOriginId, pageable)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));

        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        posts.forEach(post -> {
            postResponseDtos.add(PostResponseDtoMapper.excludeCommentsFrom(post));

        });
        return postResponseDtos;

    }

    public void removePost(String postOriginId) {
        Post post = postRepository.findByPostOriginId(postOriginId).orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_EXIST));
        validateAuthentication(post);
        postRepository.delete(post);
    }

    private void validateAuthentication(Post post) {
        String userId = AuthenticationToken.getUserId();
        if (!post.getUser().getUserId().equals(userId)) {
            throw new AuthException(AuthErrorCode.NOT_AUTHORIZED);
        }
    }
}