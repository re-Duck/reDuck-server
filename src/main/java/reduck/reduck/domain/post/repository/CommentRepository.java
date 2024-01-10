package reduck.reduck.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c join fetch c.user where c.commentOriginId = :commentOriginId")
    Optional<Comment> findByCommentOriginId(@Param("commentOriginId") String commentOriginId);

    List<Comment> findAllByPost(Post post);
}
