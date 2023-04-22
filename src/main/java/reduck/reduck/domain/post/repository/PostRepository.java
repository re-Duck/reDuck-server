package reduck.reduck.domain.post.repository;

import org.springframework.data.domain.Page;
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

    @Query("select p from Post p join fetch p.user")
    List<Post> findByPostTypeOrderByIdDesc(PostType postType, Pageable pageable);


//    findAllByPostTypeWithPage();
//    findAllByPostTypeWithOriginIdAndPage();
}
