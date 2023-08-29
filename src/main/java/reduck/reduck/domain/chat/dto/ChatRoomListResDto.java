package reduck.reduck.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ChatRoomListResDto {
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
        private String userProfileImgPath;

        public OtherUserDto(String name, String userId, String userProfileImgPath) {
            this.name = name;
            this.userId = userId;
            this.userProfileImgPath = userProfileImgPath;
        }
    }

}
