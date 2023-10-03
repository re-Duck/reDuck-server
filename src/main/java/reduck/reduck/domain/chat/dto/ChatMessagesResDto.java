package reduck.reduck.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.entity.MessageType;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessagesResDto {
    private MessageType type;
    private String messageId;
    private String message;
    private LocalDateTime messageTime;

    private String userId;
    private String name;
    private String userProfileImgPath;

}
