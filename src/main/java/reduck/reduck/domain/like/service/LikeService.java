package reduck.reduck.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.board.entity.Comment;
import reduck.reduck.domain.board.repository.CommentRepository;
import reduck.reduck.domain.like.entity.CommentLikes;
import reduck.reduck.domain.like.entity.PostLikes;
import reduck.reduck.domain.like.repository.CommentLikeCacheRepository;
import reduck.reduck.domain.like.repository.CommentLikeRepository;
import reduck.reduck.domain.like.response.PostLikesResponse;
import reduck.reduck.domain.board.entity.Post;
import reduck.reduck.domain.like.repository.PostLikeCacheRepository;
import reduck.reduck.domain.like.repository.PostLikeRepository;
import reduck.reduck.domain.board.repository.PostRepository;
import reduck.reduck.domain.rank.service.RankService;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.exception.errorcode.PostErrorCode;
import reduck.reduck.global.exception.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostLikeCacheRepository postLikeCacheRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeCacheRepository commentLikeCacheRepository;
    private final RankService rankService;

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

    @Transactional
    public void likeComment(User user, String commentOriginId) {
        Comment comment = commentRepository.findByCommentOriginId(commentOriginId).orElseThrow(() -> new NotFoundException());

        commentLikeRepository.findByUserAndComment(user, comment).ifPresentOrElse(
                commentLikes -> modifyCommentLikeStatus(commentLikes),
                () -> makeCommentLikes(comment, user)
        );
    }

    private void makeCommentLikes(Comment comment, User user) {
        CommentLikes commentLikes = CommentLikes.builder()
                .comment(comment)
                .user(user)
                .isLike(true).build();
        commentLikeRepository.save(commentLikes);
        commentLikeCacheRepository.updateLikeCount(1, comment);

    }

    private void modifyCommentLikeStatus(CommentLikes commentLikes) {
        boolean status = commentLikes.isLike();
        boolean afterStatus = !status;
        commentLikeRepository.updateStatus(afterStatus, commentLikes.getId());
        int afterCount = reflectNumberBy(afterStatus);
        commentLikeCacheRepository.updateLikeCount(afterCount, commentLikes.getComment());
    }

    private void modifyLikeStatus(PostLikes postLike) {
        boolean status = postLike.isLike();
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
                .isLike(true).build();
        postLikeRepository.save(postLikes);
        postLikeCacheRepository.updateLikeCount(1, post);
    }

    /**
     * 좋아요 누른 게시글 목록 조회
     *
     * @return
     */
    public List<PostLikesResponse> getLikePosts(User user) {
        List<PostLikes> postLikes = postLikeRepository.findAllByUser(user);
        return postLikes.stream()
                .map(postLike ->
                        PostLikesResponse.from(postLike.getPost()))
                .collect(Collectors.toList());
    }

    /**
     * 본인의 게시글의 좋아요 여부를 확인한다.
     */
    public Boolean getLikePostStatus(User user, String postOriginId) {
        Post post = postRepository.findByPostOriginId(postOriginId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        Optional<PostLikes> cat = postLikeRepository.findByUserAndPost(user, post);
        if (cat.isPresent())
            return cat.get().isLike();
        return false;
    }

    /**
     * 본인의 댓글의 좋아요 여부를 확인한다.
     */
    public Boolean getLikeCommentStatus(User user, String commentOriginId) {
        Comment comment = commentRepository.findByCommentOriginId(commentOriginId)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다."));

        Optional<CommentLikes> cat = commentLikeRepository.findByUserAndComment(user, comment);
        if (cat.isPresent())
            return cat.get().isLike();
        return false;
    }
}
