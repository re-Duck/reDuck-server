package reduck.reduck.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reduck.reduck.domain.post.entity.TemporaryPost;

public interface TemporaryPostRepository extends JpaRepository<TemporaryPost, Long> {
}
