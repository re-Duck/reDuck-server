package reduck.reduck.domain.chat.dto.mapper;

import reduck.reduck.domain.chat.dto.ChatRoomListDto;
import reduck.reduck.domain.chat.entity.ChatMessage;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.repository.UserOnly;
import reduck.reduck.domain.user.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomListDtoMapper {
    public static ChatRoomListDto of(List<User> others, ChatRoom room, ChatMessage lastChatMessage, long count) {
        List<ChatRoomListDto.OtherUserDto> otherUserDtos = others.stream()
                .map(user -> new ChatRoomListDto.OtherUserDto(user.getName(), user.getUserId(), user.getProfileImgPath()))
                .collect(Collectors.toList());

        return ChatRoomListDto.builder()
                .roomId(room.getRoomId())
                .lastChatMessage(lastChatMessage.getMessage())
                .lastChatMessageId(lastChatMessage.getMessageId())
                .lastChatMessageTime(lastChatMessage.getUpdatedAt())
                .otherUserDto(otherUserDtos)
                .unReadMessageCount(count)
                .build();

    }
}
