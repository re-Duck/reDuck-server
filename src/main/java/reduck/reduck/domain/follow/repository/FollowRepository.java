package reduck.reduck.domain.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.follow.Entity.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
}
