package reduck.reduck.domain.chatgpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.chatgpt.dto.ChatGptLogRequest;
import reduck.reduck.domain.chatgpt.dto.GptUsableCountResponse;
import reduck.reduck.domain.chatgpt.entity.ChatGpt;
import reduck.reduck.domain.chatgpt.entity.ChatGptLog;
import reduck.reduck.domain.chatgpt.repository.ChatGptLogRepository;
import reduck.reduck.domain.chatgpt.repository.ChatGptRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.util.AuthenticationToken;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptLogService {
    private final UserRepository userRepository;
    private final ChatGptRepository chatGptRepository;
    private final ChatGptLogRepository chatGptLogRepository;

    @Transactional
    public GptUsableCountResponse createLog(ChatGptLogRequest chatGptLogRequest) {
        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId).get();
        ChatGpt userGptPolicy = chatGptRepository.findByUser(user).get();
        int limitUsage = userGptPolicy.getGptMembership().getLimitUsage();

        Long gptUsage = chatGptLogRepository.countByChatGptAndDate(userGptPolicy, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (userGptPolicy.isUsable(gptUsage)) {
            ChatGptLog chatGptLog = ChatGptLog.of(chatGptLogRequest, userGptPolicy);
            chatGptLogRepository.save(chatGptLog);
            Long usableCount = limitUsage - gptUsage -1;
            return   GptUsableCountResponse.builder().remainUsageCount(usableCount).build();
        }
        throw new IllegalStateException("사용가능 횟수를 모두 소진하였습니다.");
        // 사용가능횟수 초과

    }
}
