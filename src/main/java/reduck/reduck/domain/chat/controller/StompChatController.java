package reduck.reduck.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import reduck.reduck.domain.chat.dto.ChatMessageReqDto;
import reduck.reduck.domain.chat.dto.ChatMessageResDto;
import reduck.reduck.domain.chat.entity.MessageType;
import reduck.reduck.domain.chat.service.ChatService;

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
    public void message(ChatMessageReqDto message, StompHeaderAccessor accessor, @Payload GenericMessage<?> payload) {
        System.out.println("payload = " + payload);

        System.out.println("메시지매핑 컨트롤러 ==========================================" + message.getType());
        System.out.println("세션Id 매핑 후 = " + accessor.getSessionId());
        // 입장 알림 메시지를 저장 할 필요 X
        if (message.getType().equals(MessageType.ENTER)) {
            message.setMessage(message.getUserId() + "님이 입장하셨습니다.");

            // 입장시, ChatRoomUsers에 등록 필요    .
//            simpleChatService.joinUser(message);// 그룹 챗 인 경우 필요.
        } else if (message.getType().equals(MessageType.CHAT)) {
            ChatMessageResDto chatMessageResDto = simpleChatService.sendMessage(message, payload);// save mysql
//            simpleChatService.postSend(m, message);

//            simpleChatService.joinUser();
            messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), chatMessageResDto);

        }

    }


}
