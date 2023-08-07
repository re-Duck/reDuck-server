package reduck.reduck.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomListDto {
    private String roomId;
    private String name;
    private String userId;
    private String userProfileImagePath;
    private String lastChatMessage;
    private String lastChatMessageId;
    private long unReadMessageCount;

}
