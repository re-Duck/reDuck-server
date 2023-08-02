package reduck.reduck.domain.chat.dto;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;
import reduck.reduck.domain.user.entity.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {

    private String roomId; // 클라에서 지정 후 전달.
//    private User owner; // 채팅 신청(만든) 사람.
    private String otherId; // 1:1 대화에서 상대방.
    private String name;


}