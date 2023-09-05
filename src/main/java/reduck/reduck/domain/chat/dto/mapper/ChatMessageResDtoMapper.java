package reduck.reduck.domain.chat.dto.mapper;

import reduck.reduck.domain.chat.dto.ChatMessageResDto;
import reduck.reduck.domain.chat.dto.ChatMessagesResDto;
import reduck.reduck.domain.chat.entity.ChatMessage;

import java.util.List;
import java.util.stream.Collectors;

public class ChatMessageResDtoMapper {
    public static ChatMessageResDto from(ChatMessage chatMessage) {
        return ChatMessageResDto.builder()
                .roomId(chatMessage.getRoom().getRoomId())
                .message(chatMessage.getMessage())
                .messageId(chatMessage.getMessageId())
                .messageTime(chatMessage.getUpdatedAt())
                .name(chatMessage.getSender().getName())
                .userProfileImagePath(chatMessage.getSender().getProfileImgPath())
                .type(chatMessage.getType())
                .build();
    }

}