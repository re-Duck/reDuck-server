package reduck.reduck.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.user.entity.UserProfileImg;
@Repository
public interface UserProfileImgRepository extends JpaRepository<UserProfileImg, Long> {
}
