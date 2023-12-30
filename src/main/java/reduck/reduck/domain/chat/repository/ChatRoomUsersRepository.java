package reduck.reduck.domain.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.entity.ChatRoomUsers;
import reduck.reduck.domain.user.entity.User;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomUsersRepository extends JpaRepository<ChatRoomUsers, Long> {
    Optional<ChatRoomUsers> findByRoomAndUser(ChatRoom chatRoomId, User userId);
    List<ChatRoomUsers> findAllByUser(User userId, Pageable pageable);
    List<ChatRoomUsers> findAllByRoomAndUserNot(ChatRoom room, User user);

    Optional<ChatRoomUsers> findByUserAndRoom(User user, ChatRoom chatRoom);

}
