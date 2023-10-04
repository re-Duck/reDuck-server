package reduck.reduck.domain.chatgpt.dto;

import lombok.Getter;

@Getter
public class ChatGptLogRequest {
    private String userCode;
    private String userQuestion;
    private String gptAnswer;
}
