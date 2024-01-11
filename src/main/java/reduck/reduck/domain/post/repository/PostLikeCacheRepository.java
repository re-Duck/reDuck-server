package reduck.reduck.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.like.entity.PostLikeCache;

import java.util.List;
import java.util.Optional;

public interface PostLikeCacheRepository extends JpaRepository<PostLikeCache, Long> {

    @Modifying
    @Query("update PostLikeCache plc set plc.count = plc.count + :afterCount where plc.post = :post")
    void updateLikeCount(@Param("afterCount") int afterCount, @Param("post") Post post);

    @Query("select plc from PostLikeCache plc where plc.post in :posts")
    List<PostLikeCache> findByPosts(@Param("posts") List<Post> posts);

    Optional<PostLikeCache> findByPost(Post post);
}
