package reduck.reduck.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import reduck.reduck.domain.chat.entity.MessageType;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatMessageResDto {

    private String userId;
    private String name;
    private String userProfileImagePath;
    private String message;
    private String messageId;
    private LocalDateTime messageTime;
    private MessageType type;

}
