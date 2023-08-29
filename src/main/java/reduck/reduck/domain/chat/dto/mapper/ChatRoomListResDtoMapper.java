package reduck.reduck.domain.chat.dto.mapper;

import reduck.reduck.domain.chat.dto.ChatRoomListResDto;
import reduck.reduck.domain.chat.entity.ChatMessage;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomListResDtoMapper {
    public static ChatRoomListResDto of(List<User> others, ChatRoom room, ChatMessage lastChatMessage, long count) {
        List<ChatRoomListResDto.OtherUserDto> otherUserDtos = others.stream()
                .map(user -> new ChatRoomListResDto.OtherUserDto(user.getName(), user.getUserId(), user.getProfileImgPath()))
                .collect(Collectors.toList());

        return ChatRoomListResDto.builder()
                .roomId(room.getRoomId())
                .lastChatMessage(lastChatMessage.getMessage())
                .lastChatMessageId(lastChatMessage.getMessageId())
                .lastChatMessageTime(lastChatMessage.getUpdatedAt())
                .otherUserDto(otherUserDtos)
                .unReadMessageCount(count)
                .build();

    }
}
