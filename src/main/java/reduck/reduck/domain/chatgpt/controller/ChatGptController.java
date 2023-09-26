package reduck.reduck.domain.chatgpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reduck.reduck.domain.chatgpt.service.ChatGptService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-gpt")
public class ChatGptController {
    private final ChatGptService chatGptService;


    @GetMapping("/remain-usage")
    public int getRemainingUsage(){
        return chatGptService.getRemainingUsage();
    }
}
