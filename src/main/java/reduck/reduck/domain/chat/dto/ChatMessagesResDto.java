package reduck.reduck.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.entity.MessageType;

@Getter
@Builder
public class ChatMessagesResDto {
    private MessageType type;
    private String messageId;

    private String userId;
    private String name;
    private String userProfileImgPath;

    private String message;
}
