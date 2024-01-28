package reduck.reduck.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reduck.reduck.domain.tag.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
