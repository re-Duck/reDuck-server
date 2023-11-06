package reduck.reduck.domain.chatgpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.chatgpt.entity.ChatGpt;
import reduck.reduck.domain.chatgpt.entity.ChatGptLog;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ChatGptLogRepository extends JpaRepository<ChatGptLog, Long> {
    List<ChatGptLog> findAllByChatGpt(ChatGpt chatGpt);
    Long countByChatGptAndDate(ChatGpt chatGpt, String date);
}
