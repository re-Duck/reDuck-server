package reduck.reduck.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ChatRoomListDto {
    private String roomId;
    private List<OtherUserDto> otherUserDto;
    private String lastChatMessage;
    private String lastChatMessageId;
    private LocalDateTime lastChatMessageTime;
    private long unReadMessageCount;

    @Getter
    public static class OtherUserDto implements Serializable {
        private String name;
        private String userId;
        private String userProfileImagePath;

        public OtherUserDto(String name, String userId, String userProfileImagePath) {
            this.name = name;
            this.userId = userId;
            this.userProfileImagePath = userProfileImagePath;
        }
    }

}
