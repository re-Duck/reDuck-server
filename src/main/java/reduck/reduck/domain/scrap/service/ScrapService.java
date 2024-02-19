package reduck.reduck.domain.scrap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.repository.PostRepository;
import reduck.reduck.domain.scrap.dto.ScrapPostDto;
import reduck.reduck.domain.scrap.entity.ScrapPost;
import reduck.reduck.domain.scrap.repository.ScrapRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void scrapPost(User user, String postOriginId) {
        Post post = postRepository.findByPostOriginId(postOriginId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        scrapRepository.findByUserAndPost(user, post).ifPresentOrElse(
                scrap -> unScrap(post, user),
                () -> doScrap(post, user)
        );
    }

    private void unScrap(Post post, User user) {
        scrapRepository.deleteByPostAndUser(post, user);
    }

    private void doScrap(Post post, User user) {
        ScrapPost scrapPost = ScrapPost.builder().post(post).user(user).build();
        scrapRepository.save(scrapPost);
    }

    public List<ScrapPostDto> getScrapPosts(User user) {
        List<ScrapPost> scrapPosts = scrapRepository.findAllByUser(user);

        return scrapPosts.stream()
                .map(scrap -> ScrapPostDto.from(scrap.getPost()))
                .collect(Collectors.toList());
    }

    /**
     * 본인의 게시글의 스크랩 여부를 확인한다.
     */
    public Boolean getScrapPostStatus(User user, String postOriginId) {
        Post post = postRepository.findByPostOriginId(postOriginId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        return scrapRepository.findByUserAndPost(user, post).isPresent();
    }
}
