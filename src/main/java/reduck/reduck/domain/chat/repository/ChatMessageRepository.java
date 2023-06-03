package reduck.reduck.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reduck.reduck.domain.chat.entity.ChatMessage;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<List<ChatMessage>> findAllByRoomId(String roomId);
}
