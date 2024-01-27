package reduck.reduck.domain.scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.scrap.entity.ScrapPost;
import reduck.reduck.domain.user.entity.User;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<ScrapPost, Long> {
    Optional<ScrapPost> findByUserAndPost(User user, Post post);

    void deleteByPostAndUser(Post post, User user);
}
