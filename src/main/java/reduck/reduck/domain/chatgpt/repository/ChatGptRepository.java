package reduck.reduck.domain.chatgpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.chatgpt.entity.ChatGpt;
import reduck.reduck.domain.user.entity.User;

import java.util.Optional;

@Repository
public interface ChatGptRepository extends JpaRepository<ChatGpt,Long> {
    Optional<ChatGpt> findByUser(User user);
}
