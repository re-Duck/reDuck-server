package reduck.reduck.domain.chatgpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.chatgpt.entity.ChatGpt;
import reduck.reduck.domain.chatgpt.entity.ChatGptLog;
import reduck.reduck.domain.chatgpt.repository.ChatGptLogRepository;
import reduck.reduck.domain.chatgpt.repository.ChatGptRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.util.AuthenticationToken;

@Service
@RequiredArgsConstructor
public class ChatGptService {
    private final ChatGptRepository chatGptRepository;
    private final ChatGptLogRepository chatGptLogRepository;
    private final UserRepository userRepository;

    public int getRemainingUsage() {
        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId).get();
        ChatGpt chatGpt = chatGptRepository.findByUser(user).get();
        int limitUsage = chatGpt.getPolicy().getLimitUsage();

        int usage = chatGptLogRepository.findAllByChatGpt(chatGpt).size();
        return
                limitUsage - usage;

    }
}
