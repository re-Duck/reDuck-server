package reduck.reduck.util;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.global.exception.errorcode.ChatErrorCode;
import reduck.reduck.global.exception.exception.ChatException;

@Component
public class StompHeaderAccessorUtils {

    public static String getChatRoomId(SimpMessageHeaderAccessor headerAccessor) {
        return headerAccessor.getMessageHeaders().get("simpDestination").toString().split("room/")[1];
    }

    public static String getSessionId(SimpMessageHeaderAccessor headerAccessor) {
       return headerAccessor.getMessageHeaders().get("simpSessionId").toString();

    }

    public static String resolveHeaderAccessor(SimpMessageHeaderAccessor headerAccessor) {
       return String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
    }
}
