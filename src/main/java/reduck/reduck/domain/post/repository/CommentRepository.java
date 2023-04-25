package reduck.reduck.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.post.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
