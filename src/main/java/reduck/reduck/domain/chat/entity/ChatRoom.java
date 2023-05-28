package reduck.reduck.domain.chat.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;
import reduck.reduck.domain.chat.dto.ChatMessageDto;
import reduck.reduck.domain.chat.service.ChatService;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ChatRoom {

    private String roomId;
    private String roomName;


    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();
        room.roomId = UUID.randomUUID().toString();
        room.roomName = name;
        return room;
    }
}
