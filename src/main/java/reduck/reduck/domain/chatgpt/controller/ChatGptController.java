package reduck.reduck.domain.chatgpt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import reduck.reduck.domain.chatgpt.dto.ChatGptLogRequest;
import reduck.reduck.domain.chatgpt.dto.GptUsableCountResponse;
import reduck.reduck.domain.chatgpt.entity.ChatGptMembership;
import reduck.reduck.domain.chatgpt.service.ChatGptLogService;
import reduck.reduck.domain.chatgpt.service.ChatGptService;
import reduck.reduck.global.entity.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-gpt")
public class ChatGptController {
    private final ChatGptService chatGptService;
    private final ChatGptLogService chatGptLogService;

    @GetMapping("/remain-usage")
    public Response<GptUsableCountResponse> getRemainingUsage() {
        GptUsableCountResponse remainingUsage = chatGptService.getRemainingUsage();
        return Response.successResponse(remainingUsage);
    }

    @PostMapping()
    public ResponseEntity<Response<GptUsableCountResponse>> log(
            @RequestBody ChatGptLogRequest chatGptLogRequest
    ) {
        GptUsableCountResponse remainingUsage = chatGptLogService.createLog(chatGptLogRequest);
        return new ResponseEntity<>(Response.successResponse(remainingUsage), HttpStatus.CREATED);
    }

    @PutMapping("/membership")
    public ResponseEntity<Response<Void>> changeMembership(@RequestParam ChatGptMembership membership) {
        chatGptService.changeMembership(membership);
        return new ResponseEntity<>(Response.successResponse(null), HttpStatus.OK);
    }
}
