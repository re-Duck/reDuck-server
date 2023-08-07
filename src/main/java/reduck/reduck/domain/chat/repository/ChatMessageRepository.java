package reduck.reduck.domain.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import reduck.reduck.domain.chat.entity.ChatMessage;
import reduck.reduck.domain.chat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<List<ChatMessage>> findAllByRoom(ChatRoom room);

    Optional<List<ChatMessage>> findAllByRoomOrderByIdDesc(ChatRoom room, Pageable pageable);
    Optional<List<ChatMessage>> findAllByOrderByIdDesc();

}
