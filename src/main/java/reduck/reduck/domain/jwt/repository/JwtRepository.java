package reduck.reduck.domain.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.jwt.entity.RefreshToken;

@Repository
public interface JwtRepository extends JpaRepository<RefreshToken, Long> {

}
