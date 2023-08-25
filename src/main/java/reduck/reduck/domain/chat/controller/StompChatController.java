package reduck.reduck.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reduck.reduck.domain.chat.dto.ChatMessageDto;
import reduck.reduck.domain.chat.entity.MessageType;
import reduck.reduck.domain.chat.service.ChatService;
import reduck.reduck.domain.chat.service.SimpleChatService;

import java.util.HashMap;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class StompChatController {
//    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    //    @MessageMapping(value = "/chat/enter") // 소켓통신
//    public void enter(ChatMessageDto message) {
//        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
//        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//    }
//
//    @MessageMapping(value = "/chat/message")
//    public void message(ChatMessageDto message) {
//        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
//    }
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService simpleChatService;
    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message, Message<?> m, StompHeaderAccessor accessor) {

        System.out.println("############## message.getType() = " + message.getType());
        System.out.println("세션Id 매핑 후 = " + accessor.getSessionId());
        // 입장 알림 메시지를 저장 할 필요 X
        if (message.getType().equals(MessageType.ENTER)) {
            message.setMessage(message.getUserId() + "님이 입장하셨습니다.");

            // 입장시, ChatRoomUsers에 등록 필요    .
//            simpleChatService.joinUser(message);// 그룹 챗 인 경우 필요.
        } else if (message.getType().equals(MessageType.CHAT)) {
            simpleChatService.sendMessage(message); // save mysql
            simpleChatService.postSend(m, message);

//            simpleChatService.joinUser();
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

    }


}
