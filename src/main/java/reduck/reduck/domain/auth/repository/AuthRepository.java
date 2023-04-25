package reduck.reduck.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.auth.entity.RefreshToken;

import java.util.List;

@Repository
public interface AuthRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findAllByUser_Id(Long id);

}
