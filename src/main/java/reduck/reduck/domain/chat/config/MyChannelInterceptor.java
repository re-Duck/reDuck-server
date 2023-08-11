package reduck.reduck.domain.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import reduck.reduck.global.security.JwtProvider;
import reduck.reduck.util.AuthenticationToken;

import java.util.List;

@Component

public class MyChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println(" 메시지 발신 전 인터셉트 ==============================================");

        // 메시지를 가로채서 처리할 로직 작성
        // 예: 특정 메시지 유형에 대한 처리, 권한 검사 등
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (headerAccessor.getCommand() == StompCommand.CONNECT) { // 연결 시에한 header 확인
            // JWT 토큰 검증 로직 chat서비스에 달린 JWT검증.
            String token = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));

            System.out.println("token = " + token);
        }else{

        }
        return message;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        System.out.println(" 메시지 수신 후 인터셉트 ==============================================");
        return ChannelInterceptor.super.postReceive(message, channel);
    }
}
