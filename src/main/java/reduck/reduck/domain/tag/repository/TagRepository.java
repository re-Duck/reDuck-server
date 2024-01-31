package reduck.reduck.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.tag.entity.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByPost(Post post);

    void deleteAllByPost(Post post);
}
