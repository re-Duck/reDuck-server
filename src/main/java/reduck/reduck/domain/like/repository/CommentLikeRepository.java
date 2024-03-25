package reduck.reduck.domain.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reduck.reduck.domain.board.entity.Comment;
import reduck.reduck.domain.like.entity.CommentLikes;
import reduck.reduck.domain.user.entity.User;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikes, Long> {
    Optional<CommentLikes> findByUserAndComment(User user, Comment comment);

    @Modifying
    @Query("update CommentLikes cl set cl.isLike = :afterStatus where cl.id = :id ")
    void updateStatus(@Param("afterStatus") boolean afterStatus, @Param("id") Long id);
}
