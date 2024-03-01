package reduck.reduck.domain.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostType;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p join fetch p.user left outer join fetch p.comments where p.postOriginId = :postOriginId")
    Optional<Post> findByPostOriginId(@Param("postOriginId") String postOriginId);

    @Query("select p, count(c.id) as commentsCount from Post p " +
            "join fetch p.user " +
            "left join fetch Comment as c on p = c.post " +
            "where p.postType in(:postType) " +
            "group by p.id " +
            "order by p.id desc ")
    Optional<List<Post>> findAllByPostTypeOrderByIdDescLimitPage(@Param("postType") List<PostType> postType, Pageable pageable);

    @Query("select p, count(c.id) as commentsCount from Post p " +
            "join fetch p.user " +
            "left join fetch Comment as c on p.id = c.post.id " +
            "where p.postType in(:postType) and  (select post.id from Post post where post.postOriginId = :postOriginId) > p.id " +
            "group by p.id " +
            "order by p.id desc")
    Optional<List<Post>> findAllByPostTypeAndPostOriginIdOrderByIdDescLimitPage(@Param("postType") List<PostType> postType,
                                                                      @Param("postOriginId") String postOriginId,
                                                                      Pageable pageable);
}
