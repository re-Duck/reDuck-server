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
    public GptUsableCountResponse getRemainingUsage(){
        return chatGptService.getRemainingUsage();
    }
    @PostMapping()
    public ResponseEntity<Boolean> log(
            @RequestBody ChatGptLogRequest chatGptLogRequest
    ){
        Boolean log = chatGptLogService.createLog(chatGptLogRequest);
        if(log){
            return new ResponseEntity<>(log, HttpStatus.CREATED);

        }
        return new ResponseEntity<>(log, HttpStatus.FORBIDDEN);
    }
}
