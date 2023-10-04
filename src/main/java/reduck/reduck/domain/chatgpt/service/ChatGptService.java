package reduck.reduck.domain.chatgpt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.chatgpt.dto.GptUsableCountResponse;
import reduck.reduck.domain.chatgpt.entity.ChatGpt;
import reduck.reduck.domain.chatgpt.entity.ChatGptMembership;
import reduck.reduck.domain.chatgpt.repository.ChatGptLogRepository;
import reduck.reduck.domain.chatgpt.repository.ChatGptRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.util.AuthenticationToken;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ChatGptService {
    private final ChatGptRepository chatGptRepository;
    private final ChatGptLogRepository chatGptLogRepository;
    private final UserRepository userRepository;

    public GptUsableCountResponse getRemainingUsage() {
        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId).get();
        ChatGpt chatGpt = chatGptRepository.findByUser(user).get();
        int limitUsage = chatGpt.getGptMembership().getLimitUsage();

//        int usage = chatGptLogRepository.findAllByChatGpt(chatGpt).size();
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Long usage = chatGptLogRepository.countByChatGptAndDate(chatGpt, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Long usableCount = limitUsage - usage;
        return GptUsableCountResponse.builder().remainUsageCount(usableCount).build();

    }

    @Transactional
    public void changeMembership(ChatGptMembership toMembership) {
        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId).get();
        ChatGpt chatGpt = chatGptRepository.findByUser(user).get();
        chatGpt.upgradeMembership(toMembership);
        chatGptRepository.save(chatGpt);
    }
}
