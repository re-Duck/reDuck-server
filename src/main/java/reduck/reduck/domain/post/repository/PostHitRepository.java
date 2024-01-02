package reduck.reduck.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostHit;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostHitRepository extends JpaRepository<PostHit, Long> {
    Optional<PostHit> findByPost(Post post);

    @Modifying
    @Query("update PostHit  h set h.hits = h.hits + 1 where h.post = :post")
    int updateHits(@Param("post") Post post);

    @Query("select h from PostHit h where h.post.id in :ids")
    List<PostHit> findAllByIds(@Param("ids") List<Long> ids);

}
