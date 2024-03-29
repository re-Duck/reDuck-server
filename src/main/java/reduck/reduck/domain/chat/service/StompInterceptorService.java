package reduck.reduck.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.entity.Session;
import reduck.reduck.domain.chat.repository.ChatRoomRepository;
import reduck.reduck.domain.chat.repository.SessionRepository;
import reduck.reduck.global.exception.errorcode.ChatErrorCode;
import reduck.reduck.global.exception.exception.NotFoundException;
import reduck.reduck.util.StompAuth;

import jakarta.transaction.Transactional;

import java.util.Map;

import static reduck.reduck.util.StompHeaderAccessorUtils.*;

@Service
@RequiredArgsConstructor
public class StompInterceptorService {
    private final SessionRepository sessionRepository;
    private final ChatRoomRepository chatRoomRepository;
    private String account;

    public void connect(SimpMessageHeaderAccessor headerAccessor) {
        // 연결 시에한 header 확인
        // JWT 토큰 검증 로직 chat서비스에 달린 JWT검증.
        String token = resolveHeaderAccessor(headerAccessor);
        String jwt = token.split(" ")[1];
        account = StompAuth.getAccount(jwt);
    }

    @Transactional
    public void disconnect(StompHeaderAccessor headerAccessor) {
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        String sessionId = headerAccessor.getSessionId();
//        ChatRoom chatRoom = getChatRoom(headerAccessor);
        // 소켓 끊김 시, DB의 세션 관리.
        String o = (String) sessionAttributes.get(sessionId); // unchecked
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(o).get();
        Session session = sessionRepository.findBySessionIdAndRoom(sessionId, chatRoom).orElseThrow(() -> new NotFoundException(ChatErrorCode.SESSION_NOT_EXIST));
//        Session session = sessionRepository.findBySessionId(sessionId).orElseThrow(() -> new ChatException(ChatErrorCode.SESSION_NOT_EXIST));
        session.off();
        sessionRepository.save(session);
    }

    private ChatRoom getChatRoom(StompHeaderAccessor headerAccessor) {
        String roomId = getChatRoomId(headerAccessor);
        return chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new NotFoundException(ChatErrorCode.CHAT_ROOM_NOT_EXIST));
    }


    @Transactional
    public void subscribe(StompHeaderAccessor headerAccessor) {
        // sessionId를 update.
//        if (headerAccessor.getCommand() == StompCommand.SUBSCRIBE) { // 연결 시에한 header 확인
////            // JWT 토큰 검증 로직 chat서비스에 달린 JWT검증.
//            String token = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
//            System.out.println("token = " + token);
//        }
//            String userId = AuthenticationToken.getUserId();

        // 세션 중복 가능.

        // 구독시, sessionAttribute에 roomId저장
        // simpdestnation에도 roomId.

        //구취시, sessionId + attribute의 roomId 사용 -> 세션 상태 업데이트해야할 세션 특정.
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        String simpSessionId = getSessionId(headerAccessor);
        ChatRoom chatRoom = getChatRoom(headerAccessor);
        Session session = sessionRepository.findByUserIdAndRoom(account, chatRoom)
                .orElseGet(() -> Session.init(simpSessionId, account, chatRoom));
        session.on();
        session.updateSessionId(simpSessionId);

        sessionAttributes.put(simpSessionId, chatRoom.getRoomId());

        sessionRepository.save(session);
    }

    public void message() {
    }
}
