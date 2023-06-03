package reduck.reduck.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.chat.dto.ChatRoomDto;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.user.entity.User;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<List<ChatRoom>> findAllByUserId(String userId);
}
