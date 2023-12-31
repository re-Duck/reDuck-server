package reduck.reduck.domain.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.follow.Entity.Follow;
import reduck.reduck.domain.user.entity.User;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findAllByUser(User user);

    List<Follow> findAllByFollowingUser(User user);
}
