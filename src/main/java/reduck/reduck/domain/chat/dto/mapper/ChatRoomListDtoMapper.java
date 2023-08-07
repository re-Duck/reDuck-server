package reduck.reduck.domain.chat.dto.mapper;

import reduck.reduck.domain.chat.dto.ChatRoomListDto;
import reduck.reduck.domain.chat.entity.ChatMessage;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.user.entity.User;

public class ChatRoomListDtoMapper {
    public static ChatRoomListDto of(User user, ChatRoom room, ChatMessage lastChatMessage, long count) {
       return ChatRoomListDto.builder()
                .roomId(room.getRoomId())
                .lastChatMessage(lastChatMessage.getMessage())
               .lastChatMessageId(lastChatMessage.getMessageId())
                .name(user.getName())
                .userId(user.getUserId())
                .userProfileImagePath(user.getProfileImgPath())
                .unReadMessageCount(count)
                .build();

    }
}
