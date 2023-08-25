package reduck.reduck.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ChatRoomResDto {

    private List<ChatMessagesResDto> chatMessages;
}
