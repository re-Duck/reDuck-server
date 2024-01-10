package reduck.reduck.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.post.dto.PostDetailResponseDto;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.dto.mapper.PostDetailResponseDtoMapper;
import reduck.reduck.domain.post.entity.*;
import reduck.reduck.domain.post.entity.mapper.PostMapper;
import reduck.reduck.domain.post.dto.mapper.PostResponseDtoMapper;
import reduck.reduck.domain.post.repository.PostLikeCacheRepository;
import reduck.reduck.domain.post.repository.PostLikeRepository;
import reduck.reduck.domain.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import reduck.reduck.domain.post.repository.PostHitRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.entity.UserProfileImg;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.AuthErrorCode;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.PostErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.*;
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
    private final PostHitRepository postHitRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostLikeCacheRepository postLikeCacheRepository;
    private final UserRepository userRepository;
    private final String PATH = "C:\\reduckStorage\\post";
    private static final String DEV_PATH = "/home/ubuntu/reduck/storage/post";

    public String mayackImage(String account, MultipartFile file) {
        String mayackPath = DEV_PATH + "/mayack";
        return saveMayackPostImage(mayackPath, file, account);
    }

    private String saveMayackPostImage(String myackPath, MultipartFile multipartFile, String userId) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.split("\\.")[1];
        String storageFileName = UUID.randomUUID() + "." + extension;
        long size = multipartFile.getSize();
        String path = myackPath + "/" + userId; //폴더 경로
        File Folder = new File(path);
        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try {
                Folder.mkdir(); //폴더 생성합니다.
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        Path imagePath = Paths.get(path, storageFileName);
        try {
            UserProfileImg userProfileImg = UserProfileImg.builder()
                    .storagedFileName(storageFileName)
                    .uploadedFileName(originalFilename)
                    .path(String.valueOf(imagePath))
                    .extension(extension)
                    .size(size)
                    .build();
            Files.write(imagePath, multipartFile.getBytes());
            return String.valueOf(imagePath);

        } catch (Exception e) {
            log.error("이미지 저장 실패", e);
            throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void createPost(PostDto postDto) {
        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_EXIST));
        Post postEntity = PostMapper.from(postDto);
        postEntity.setUser(user);


        // 조회수 테이블 초기화.
        PostHit readCount = PostHit.builder()
                .hits(0)
                .post(postEntity)
                .build();
        postEntity.setPostHit(readCount);
        postRepository.save(postEntity);

        PostLikeCache postLikeCache = PostLikeCache.builder()
                .post(postEntity)
                .count(0).build();
        postHitRepository.save(readCount);
        postLikeCacheRepository.save(postLikeCache);
    }

    @Transactional
    public String saveMultipartFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }
        Path imagePath = makeStorageFilePath(multipartFile);
        try {
            Files.write(imagePath, multipartFile.getBytes());
            return String.valueOf(imagePath);
        } catch (Exception e) {
            log.error("이미지 저장 실패", e);
            throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private Path makeStorageFilePath(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.split("\\.")[1];
        String storageFileName = UUID.randomUUID() + "." + extension;
        String path = DEV_PATH + "/" + AuthenticationToken.getUserId(); //폴더 경로
        File Folder = new File(path);
        // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
        if (!Folder.exists()) {
            try {
                Folder.mkdir(); //폴더 생성합니다.
            } catch (Exception e) {
                e.getStackTrace();
                throw new CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
        return Paths.get(path, storageFileName); //local용

    }

    @Transactional
    public PostDetailResponseDto findByPostOriginId(String postOriginId) {
        Post post = postRepository.findByPostOriginId(postOriginId)
                .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));

        postHitRepository.updateHits(post);
        PostDetailResponseDto postDetailResponseDto = PostDetailResponseDtoMapper.from(post);
        return postDetailResponseDto;
    }

    // paging의 경우.
    // DB에 post가 Long id순으로 삽임됨 == createdAt순
    // 단순 page만 있으면 최신순으로 page갯수만큼 조회
    public List<PostResponseDto> getPosts(String postOriginId, List<String> types, int page) {
        List<PostType> postTypes = types
                .stream()
                .map(PostType::getType)
                .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(0, page);
        List<Post> posts;
        if (postOriginId == "") {
            posts = postRepository.findAllByPostTypeOrderByIdDescLimitPage(postTypes, pageable)
                    .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        } else {
            posts = postRepository.findAllByPostTypeAndPostOriginIdOrderByIdDescLimitPage(postTypes, postOriginId, pageable)
                    .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        }
        List<PostLikeCache> postLikes = postLikeCacheRepository.findByPosts(posts);

        List<PostResponseDto> dtos = new ArrayList<>();

        for (Post post : posts) {
            for (PostLikeCache plc : postLikes) {
                if (post.getId() == plc.getPost().getId()) {
                    dtos.add(PostResponseDtoMapper.of(post, plc.getCount()));
                    break;
                }
            }
        }
        return dtos;
//        List<PostResponseDto> postResponseDtos = posts.stream()
//                .map(PostResponseDtoMapper::from)
//                .collect(Collectors.toList());
    }

    public void removePost(String postOriginId) {
        Post post = postRepository.findByPostOriginId(postOriginId).orElseThrow(
                () -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        validateAuthentication(post);
        postRepository.delete(post);
    }

    public void updatePost(String postOriginId, PostDto postDto) {
        Post post = postRepository.findByPostOriginId(postOriginId).orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        validateAuthentication(post);
        post.updateFrom(postDto);
        postRepository.save(post);
    }

    private void validateAuthentication(Post post) {
        String userId = AuthenticationToken.getUserId();
        if (!post.getUser().getUserId().equals(userId)) {
            throw new AuthException(AuthErrorCode.NOT_AUTHORIZED);
        }
    }

    /**
     * 게시글 좋아요 기능
     */
    @Transactional
    public void like(User user, String postOriginId) {
        Post post = postRepository.findByPostOriginId(postOriginId)
                .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));

        postLikeRepository.findByUserAndPost(user, post).ifPresentOrElse(
                postLike -> modifyLikeStatus(postLike),
                () -> makeLike(post, user)
        ); // post - user 중간 테이블에 좋아요 상태 반영
    }

    private void modifyLikeStatus(PostLikes postLike) {
        boolean status = postLike.isStatus();
        boolean afterStatus = !status;
        postLikeRepository.updateStatus(afterStatus, postLike.getId());
        int afterCount = reflectNumberBy(afterStatus);
        postLikeCacheRepository.updateLikeCount(afterCount, postLike.getPost());
    }

    private int reflectNumberBy(boolean status) {
        if (status) return 1;
        return -1;
    }

    private void makeLike(Post post, User user) {
        PostLikes postLikes = PostLikes.builder()
                .post(post)
                .user(user)
                .status(true).build();
        postLikeRepository.save(postLikes);
        postLikeCacheRepository.updateLikeCount(1, post);
    }
}