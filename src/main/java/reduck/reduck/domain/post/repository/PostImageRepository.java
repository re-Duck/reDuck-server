package reduck.reduck.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.post.entity.PostImage;
@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
