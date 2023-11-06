package reduck.reduck.domain.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import reduck.reduck.domain.chat.entity.ChatMessage;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.PostType;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<List<ChatMessage>> findAllByRoom(ChatRoom room);

    List<ChatMessage> findAllByRoomOrderByIdDesc(ChatRoom room, Pageable pageable);
    Optional<List<ChatMessage>> findAllByOrderByIdDesc();

    Optional<ChatMessage> findByMessageId(String messageId);

    @Query("select cm from ChatMessage cm where cm.room= :room and (select cm2.id from ChatMessage cm2 where cm2.messageId = :messageId) > cm.id order by cm.id desc")
    List<ChatMessage> find2(@Param("messageId") String messageId, Pageable pageable, @Param("room") ChatRoom room);


}
