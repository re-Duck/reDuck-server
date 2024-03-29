package reduck.reduck.domain.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import reduck.reduck.domain.chat.service.StompInterceptorService;

import java.security.Principal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StompChannelInterceptor implements ChannelInterceptor {
    private final StompInterceptorService interceptorService;

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        System.out.println();
        System.out.println("메시지 발신 후 인터셉트@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println();

    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println(" 메시지 발신 전 인터셉트 ==============================================");


        // 메시지를 가로채서 처리할 로직 작성
        // 예: 특정 메시지 유형에 대한 처리, 권한 검사 등
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String sessionId = headerAccessor.getSessionId();
        System.out.println("sessionId = " + sessionId);
        StompCommand command = headerAccessor.getCommand();
        System.out.println("command = " + command);
        String message1 = headerAccessor.getMessage();
        System.out.println("message1 = " + message1);
        String login = headerAccessor.getLogin();
        System.out.println("login = " + login);
        Principal user = headerAccessor.getUser();
        System.out.println("user = " + user);
        SimpMessageType messageType = headerAccessor.getMessageType();
        System.out.println("messageType = " + messageType);
        MessageHeaders messageHeaders = headerAccessor.getMessageHeaders();
        System.out.println("messageHeaders = " + messageHeaders);

        operatedOf(command, headerAccessor);

//        if (headerAccessor.getCommand() == StompCommand.CONNECT) { // 연결 시에한 header 확인
//            // JWT 토큰 검증 로직 chat서비스에 달린 JWT검증.
//            String token = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
//
//            System.out.println("token = " + token);
//        }
//        else{
//
//        }
//        channel.send(message);
        return message;
    }

    private void operatedOf(StompCommand command, StompHeaderAccessor headerAccessor) {
        switch (command) {
            case CONNECT:
                System.out.println("command = " + command);
                interceptorService.connect(headerAccessor);
                break;
            case DISCONNECT: // 정상 || 비정상 소켓 끊김.
                System.out.println("소켓 끊김.");
                System.out.println("command = " + command);
                interceptorService.disconnect(headerAccessor);
                break;
            case SUBSCRIBE:
                System.out.println("command = " + command);
                System.out.println("변경 전 headerAccessor.getSessionId() = " + headerAccessor.getSessionId());
                interceptorService.subscribe(headerAccessor); // 새로 할당된 sessionId 로 업데이트 (socket session id는 웹 핸드쉐이크 하는 과정의 session으로 copy됨)
                System.out.println("변경 후 headerAccessor.getSessionId() = " + headerAccessor.getSessionId());
                break;
            case SEND:
                System.out.println("command = " + command);
                break;
            case UNSUBSCRIBE:
                System.out.println("소켓 끊김2.");
                System.out.println("command2 = " + command);
                interceptorService.disconnect(headerAccessor);
                break;
        }
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        System.out.println(" 메시지 수신 후 인터셉트 ==============================================");
        return ChannelInterceptor.super.postReceive(message, channel);
    }
}
