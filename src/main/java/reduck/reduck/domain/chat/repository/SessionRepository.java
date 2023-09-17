package reduck.reduck.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.entity.Session;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {
    Optional<Session> findByUserIdAndRoom(String userId, ChatRoom room);

    Optional<Session> findBySessionId(String sessionId);

    Optional<Session> findBySeesionIdAndRoom(String sessionId, ChatRoom room);
}
