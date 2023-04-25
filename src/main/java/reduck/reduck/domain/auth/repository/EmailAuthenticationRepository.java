package reduck.reduck.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import reduck.reduck.domain.auth.entity.EmailAuthentication;

import java.util.Optional;

@Repository
public interface EmailAuthenticationRepository extends JpaRepository<EmailAuthentication, Long> {
    Optional<EmailAuthentication> findTopByEmailOrderByIdDesc(String email);

}
