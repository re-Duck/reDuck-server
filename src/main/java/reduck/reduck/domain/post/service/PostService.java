package reduck.reduck.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reduck.reduck.domain.like.entity.PostLikeCache;
import reduck.reduck.domain.post.dto.PostDetailResponseDto;
import reduck.reduck.domain.post.dto.PostDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.dto.TemporaryPostResponse;
import reduck.reduck.domain.post.dto.mapper.PostDetailResponseDtoMapper;
import reduck.reduck.domain.post.entity.*;
import reduck.reduck.domain.post.entity.mapper.PostMapper;
import reduck.reduck.domain.post.dto.mapper.PostResponseDtoMapper;
import reduck.reduck.domain.post.repository.*;
import lombok.extern.slf4j.Slf4j;
import reduck.reduck.domain.scrap.repository.ScrapRepository;
import reduck.reduck.domain.tag.dto.TagDto;
import reduck.reduck.domain.tag.entity.Tag;
import reduck.reduck.domain.tag.entity.TemporaryTag;
import reduck.reduck.domain.tag.mapper.TagMapper;
import reduck.reduck.domain.tag.mapper.TemporaryPostMapper;
import reduck.reduck.domain.tag.repository.TagRepository;
import reduck.reduck.domain.tag.repository.TemporaryTagRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.AuthErrorCode;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.PostErrorCode;
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
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final TemporaryPostRepository temporaryPostRepository;
    private final TagRepository tagRepository;
    private final TemporaryTagRepository temporaryTagRepository;
    private final PostHitRepository postHitRepository;
    private final PostLikeCacheRepository postLikeCacheRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final String PATH = "C:\\reduckStorage\\post";
    private static final String DEV_PATH = "/home/ubuntu/reduck/storage/post";


    @Transactional
    public void createPost(User user, PostDto postDto) {
        Post postEntity = PostMapper.from(postDto);
        postEntity.setUser(user);
        postRepository.save(postEntity);
        // after post save
        afterCreatePost(postEntity, postDto);
    }

    /**
     * 게시글 저장 후 벌어져야 하는 동작들 수행
     * <p>
     * 조회수, 좋아요 수 초기화
     * 게시글 태그 키워드 초기화
     */
    private void afterCreatePost(Post postEntity, PostDto postDto) {
        initHits(postEntity);
        initLikes(postEntity);
        initTags(postEntity, postDto.getTags());
    }

    private void initHits(Post postEntity) {
        PostHit readCount = PostHit.builder()
                .hits(1)
                .post(postEntity)
                .build();
        postEntity.setPostHit(readCount);
        postHitRepository.save(readCount);
    }

    private void initLikes(Post postEntity) {
        PostLikeCache postLikeCache = PostLikeCache.builder()
                .post(postEntity)
                .count(0).build();
        postLikeCacheRepository.save(postLikeCache);
    }

    private void initTags(Post postEntity, List<TagDto> tagDtos) {
        List<Tag> tags = tagDtos.stream()
                .map(tag -> TagMapper.of(postEntity, tag))
                .collect(Collectors.toList());
        tagRepository.saveAll(tags);
    }

    /**
     * 게시글 임시저장
     */
    @Transactional
    public void createTemporaryPost(User user, PostDto postDto) {
        TemporaryPost temporaryPost = TemporaryPostMapper.of(postDto, user);
        temporaryPostRepository.save(temporaryPost);

        afterCreateTemporaryPost(temporaryPost, postDto);
    }

    private void afterCreateTemporaryPost(TemporaryPost temporaryPost, PostDto dto) {
        initTemporaryTags(temporaryPost, dto.getTags());
    }

    private void initTemporaryTags(TemporaryPost temporaryPost, List<TagDto> tagDtos) {
        List<TemporaryTag> tags = tagDtos.stream()
                .map(tag -> TagMapper.of(temporaryPost, tag))
                .collect(Collectors.toList());
        temporaryTagRepository.saveAll(tags);
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
    public PostDetailResponseDto findByPostOriginId(String postOriginId, User user) {
        Post post = postRepository.findByPostOriginId(postOriginId)
                .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));

        if (user != null && user.getUserId().equals(post.getUser().getUserId())) {
        } else
            postHitRepository.updateHits(post);

        PostDetailResponseDto postDetailResponseDto = PostDetailResponseDtoMapper.from(post);

        PostLikeCache postLikeCache = postLikeCacheRepository.findByPost(post)
                .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));

        postDetailResponseDto.setLikes(postLikeCache.getCount());
        return postDetailResponseDto;
    }

    /**
     * 게시글들을 cursor 기반 페이징 조회 방식으로 반환한다.
     * @param postOriginId 기준 cursor로 사용
     * @param types 조회 할 게시글의 타입
     * @param page 조회 할 게시글의 갯수
     */
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
                    List<Tag> tags = tagRepository.findAllByPost(post);
                    dtos.add(PostResponseDtoMapper.of(post, plc.getCount(),tags));
                    break;
                }
            }
        }



        return dtos;
    }

    @Transactional
    public void removePost(String postOriginId, User user) {
        Post post = postRepository.findByPostOriginId(postOriginId).orElseThrow(
                () -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        validateAuthentication(post, user);
        cascadeBeforeRemovePost(post, user);

        postRepository.delete(post);
    }

    /**
     * 게시글 삭제 전 연관 엔티티들 삭제.
     * <p>
     * 좋아요, 조회수, 태그, 스크랩
     */
    private void cascadeBeforeRemovePost(Post post, User user) {
        deleteLikesCache(post);
        deleteLikes(post);
        deleteScraps(post, user);
        deleteTags(post);
    }

    private void deleteLikesCache(Post post) {
        PostLikeCache postLikeCache = postLikeCacheRepository.findByPost(post)
                .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        postLikeCacheRepository.delete(postLikeCache);
    }

    private void deleteLikes(Post post) {
        postLikeRepository.deleteByPost(post);
    }

    private void deleteScraps(Post post, User user) {
        scrapRepository.findByUserAndPost(user, post)
                .ifPresent(scrap -> scrapRepository.delete(scrap));
    }

    private void deleteTags(Post post) {
        List<Tag> tags = tagRepository.findAllByPost(post);
        tagRepository.deleteAll(tags);
    }

    @Transactional
    public void updatePost(String postOriginId, PostDto postDto, User user) {
        Post post = postRepository.findByPostOriginId(postOriginId).orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        validateAuthentication(post, user);
        post.updateFrom(postDto);
        afterUpdatePost(post, postDto.getTags());
        postRepository.save(post);
    }

    private void afterUpdatePost(Post post, List<TagDto> tagDtos) {
        tagRepository.deleteAllByPost(post);
        List<Tag> tags = tagDtos.stream()
                .map(tag -> TagMapper.of(post, tag))
                .collect(Collectors.toList());
        tagRepository.saveAll(tags);
    }

    private void validateAuthentication(Post post, User currentUser) {
        if (!post.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new AuthException(AuthErrorCode.NOT_AUTHORIZED);
        }
    }

    /**
     * 임시저장된 게시글 목록 조회
     */
    public List<TemporaryPostResponse> getTemporaryPosts(User user, Optional<String> temporaryPostOriginId, Pageable pageable) {
        List<TemporaryPost> result = temporaryPostRepository.findAllByUserOrderByIdDescLimitPage(user, pageable);
        return result.stream().map(TemporaryPostResponse::from).collect(Collectors.toList());
    }


    /**
     * 임시 게시글 삭제
     */
    @Transactional
    public void removeTemporaryPost(User user, String temporaryPostOriginId) {
        TemporaryPost temporaryPost = temporaryPostRepository.findByPostOriginId(temporaryPostOriginId)
                .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        if (!temporaryPost.getUser().equals(user)) {
            throw new AuthException(AuthErrorCode.FORBIDDEN);
        }
        cascadeBeforeRemoveTemporaryPost(temporaryPost);
        temporaryPostRepository.delete(temporaryPost);
    }

    private void cascadeBeforeRemoveTemporaryPost(TemporaryPost temporaryPost) {
        List<TemporaryTag> tags = temporaryTagRepository.findAllByTemporaryPost(temporaryPost);
        temporaryTagRepository.deleteAll(tags);
    }

    /**
     * 임시저장 게시글 단일 조회
     */
    public TemporaryPostResponse getTemporaryPost(String temporaryPostOriginId) {
        TemporaryPost temporaryPost = temporaryPostRepository.findByPostOriginId(temporaryPostOriginId)
                .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        return TemporaryPostResponse.from(temporaryPost);
    }

    /**
     * 임시 저장 게시글 수정
     */
    @Transactional
    public void updateTemporaryPost(User user, String temporaryPostOriginId, PostDto postDto) {
        TemporaryPost temporaryPost = temporaryPostRepository.findByPostOriginId(temporaryPostOriginId)
                .orElseThrow(() -> new NotFoundException(PostErrorCode.POST_NOT_EXIST));
        if (!temporaryPost.getUser().equals(user)) {
            throw new AuthException(AuthErrorCode.FORBIDDEN);
        }
        temporaryPost.updateFrom(postDto);
        afterUpdateTemporaryPost(temporaryPost, postDto.getTags());
    }

    private void afterUpdateTemporaryPost(TemporaryPost temporaryPost, List<TagDto> tagDtos) {
        temporaryTagRepository.deleteAllByTemporaryPost(temporaryPost);
        List<TemporaryTag> tags = tagDtos.stream()
                .map(tag -> TagMapper.of(temporaryPost, tag))
                .collect(Collectors.toList());
        temporaryTagRepository.saveAll(tags);
    }
}