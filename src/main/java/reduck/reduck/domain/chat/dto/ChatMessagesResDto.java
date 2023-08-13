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
    private ChatRoom chatRoom;

    private String userId;
    private String name;
    private String userProfileImagePath;

    private String message;
}
