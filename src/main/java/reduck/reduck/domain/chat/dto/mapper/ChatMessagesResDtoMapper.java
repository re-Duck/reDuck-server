package reduck.reduck.domain.chat.dto.mapper;

import reduck.reduck.domain.chat.dto.ChatMessagesResDto;
import reduck.reduck.domain.chat.entity.ChatMessage;

import java.util.List;
import java.util.stream.Collectors;

public class ChatMessagesResDtoMapper {
    public static List<ChatMessagesResDto> from(List<ChatMessage> chatMessages) {
        return chatMessages.stream().map(chatMessage -> {
            return ChatMessagesResDto.builder()
                    .type(chatMessage.getType())
                    .messageId(chatMessage.getMessageId())
                    .chatRoom(chatMessage.getRoom())

                    .userId(chatMessage.getSender().getUserId())
                    .name(chatMessage.getSender().getName())
                    .userProfileImagePath(chatMessage.getSender().getProfileImgPath())

                    .message(chatMessage.getMessage())
                    .build();
        }).collect(Collectors.toList());
    }

}
