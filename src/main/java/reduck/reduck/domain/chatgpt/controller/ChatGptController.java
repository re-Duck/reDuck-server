package reduck.reduck.domain.chatgpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import reduck.reduck.domain.chatgpt.dto.ChatGptLogRequest;
import reduck.reduck.domain.chatgpt.dto.GptUsableCountResponse;
import reduck.reduck.domain.chatgpt.service.ChatGptLogService;
import reduck.reduck.domain.chatgpt.service.ChatGptService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-gpt")
public class ChatGptController {
    private final ChatGptService chatGptService;
    private final ChatGptLogService chatGptLogService;


    @GetMapping("/remain-usage")
    public GptUsableCountResponse getRemainingUsage() {
        return chatGptService.getRemainingUsage();
    }

    @PostMapping()
    public ResponseEntity<Void> log(
            @RequestBody ChatGptLogRequest chatGptLogRequest
    ) {
        try {
            chatGptLogService.createLog(chatGptLogRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (IllegalStateException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        }

    }
}
