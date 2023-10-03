package reduck.reduck.domain.chatgpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.chatgpt.dto.ChatGptLogRequest;
import reduck.reduck.domain.chatgpt.entity.ChatGpt;
import reduck.reduck.domain.chatgpt.entity.ChatGptLog;
import reduck.reduck.domain.chatgpt.repository.ChatGptLogRepository;
import reduck.reduck.domain.chatgpt.repository.ChatGptRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.util.AuthenticationToken;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptLogService {
    private final UserRepository userRepository;
    private final ChatGptRepository chatGptRepository;
    private final ChatGptLogRepository chatGptLogRepository;
    @Transactional
    public Boolean createLog(ChatGptLogRequest chatGptLogRequest) {
        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId).get();
        ChatGpt userGptPolicy = chatGptRepository.findByUser(user).get();
        List<ChatGptLog> allByChatGpt = chatGptLogRepository.findAllByChatGpt(userGptPolicy);
        int gptUsage = allByChatGpt.size();

        if(userGptPolicy.isUsable(gptUsage)){
            ChatGptLog chatGptLog = ChatGptLog.of(chatGptLogRequest, userGptPolicy);
            chatGptLogRepository.save(chatGptLog);
            return true;
        }
        // 사용가능횟수 초과
        return false;

    }
}
