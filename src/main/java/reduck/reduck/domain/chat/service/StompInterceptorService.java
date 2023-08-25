package reduck.reduck.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.entity.Session;
import reduck.reduck.domain.chat.repository.ChatRoomRepository;
import reduck.reduck.domain.chat.repository.SessionRepository;
import reduck.reduck.global.exception.errorcode.ChatErrorCode;
import reduck.reduck.global.exception.exception.ChatException;
import reduck.reduck.util.AuthenticationToken;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StompInterceptorService {
    private final SessionRepository sessionRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void connect(StompHeaderAccessor sessionId) {

    }

    @Transactional
    public void disconnect(StompHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        // 소켓 끊김 시, DB의 세션 관리.
        Session session = sessionRepository.findBySessionId(sessionId).orElseThrow(() -> new ChatException(ChatErrorCode.SESSION_NOT_EXIST));
        session.off();
        sessionRepository.save(session);

    }

    @Transactional

    public void subscribe(StompHeaderAccessor headerAccessor) {
        // sessionId를 update.
//        if (headerAccessor.getCommand() == StompCommand.SUBSCRIBE) { // 연결 시에한 header 확인
////            // JWT 토큰 검증 로직 chat서비스에 달린 JWT검증.
//            String token = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
////
//            System.out.println("token = " + token);
////        }
//            String userId = AuthenticationToken.getUserId();
        String userId = "test";
        String simpSessionId = headerAccessor.getMessageHeaders().get("simpSessionId").toString();
        System.out.println("simpSessionId = " + simpSessionId);
        ChatRoom chatRoom = getChatRoom(headerAccessor);
        System.out.println("chatRoom = " + chatRoom);

        Session session = sessionRepository.findByUserIdAndRoomId(userId, chatRoom)
                .orElseGet(() -> Session.init(simpSessionId, userId, chatRoom));
        System.out.println(session);
        session.on();
        session.update(simpSessionId);
        sessionRepository.save(session);
    }


    public void message() {
    }

    private ChatRoom getChatRoom(StompHeaderAccessor headerAccessor) {
        String roomId = headerAccessor.getMessageHeaders().get("simpDestination").toString().split("room/")[1];
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_EXIST));
        return chatRoom;
    }
}
