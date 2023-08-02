package reduck.reduck.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;
import reduck.reduck.domain.chat.entity.MessageType;

@Getter
@Setter
public class ChatMessageDto {

    private String roomId;
    private String userId;
    private String message;
    private MessageType type;

}