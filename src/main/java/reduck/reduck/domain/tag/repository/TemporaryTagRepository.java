package reduck.reduck.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reduck.reduck.domain.post.entity.TemporaryPost;
import reduck.reduck.domain.tag.entity.TemporaryTag;

import java.util.List;

public interface TemporaryTagRepository extends JpaRepository<TemporaryTag, Long> {
    List<TemporaryTag> findAllByTemporaryPost(TemporaryPost temporaryPost);

    void deleteAllByTemporaryPost(TemporaryPost temporaryPost);
}
