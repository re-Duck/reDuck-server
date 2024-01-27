package reduck.reduck.domain.scrap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.repository.PostRepository;
import reduck.reduck.domain.scrap.entity.ScrapPost;
import reduck.reduck.domain.scrap.repository.ScrapRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.global.exception.exception.NotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final PostRepository postRepository;

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
}
