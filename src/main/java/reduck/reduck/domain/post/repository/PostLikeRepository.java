package reduck.reduck.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostLikes;
import reduck.reduck.domain.user.entity.User;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikes, Long> {
    Optional<PostLikes> findByUserAndPost(User user, Post post);

    @Modifying
    @Query("update PostLikes pl set pl.status = :afterStatus where pl.id = :id ")
    void updateStatus(@Param("afterStatus") boolean afterStatus, @Param("id") Long id);
}
