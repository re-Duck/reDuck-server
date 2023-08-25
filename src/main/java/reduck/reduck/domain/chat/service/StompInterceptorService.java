package reduck.reduck.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.chat.entity.Session;
import reduck.reduck.domain.chat.repository.SessionRepository;
import reduck.reduck.util.AuthenticationToken;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StompInterceptorService {
    private final SessionRepository sessionRepository;

    public void connect(StompHeaderAccessor sessionId) {

    }

    public void disconnect(StompHeaderAccessor headerAccessor) {
        // 소켓 끊김 시, DB의 세션 관리.

        String userId = "test1";
        Optional<Session> byUserId = sessionRepository.findByUserId(userId);
        Session session = byUserId.get();
        session.off();
        sessionRepository.save(session);

    }
    @Transactional

    public void subscribe(StompHeaderAccessor headerAccessor) {
        // 최초 연결 시 sessionId 고정.
//        if (headerAccessor.getCommand() == StompCommand.SUBSCRIBE) { // 연결 시에한 header 확인
////            // JWT 토큰 검증 로직 chat서비스에 달린 JWT검증.
//            String token = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
////
//            System.out.println("token = " + token);
////        }
//            String userId = AuthenticationToken.getUserId();
        String userId = "test1";
        String simpSessionId = headerAccessor.getMessageHeaders().get("simpSessionId").toString();
        System.out.println("simpSessionId = " + simpSessionId);
        String simpDestination = headerAccessor.getMessageHeaders().get("simpDestination").toString().split("room/")[1];
        System.out.println("simpDestination = " + simpDestination);
        Session session = sessionRepository.findByUserId(userId)
                .orElseGet(() -> Session.init(simpSessionId, userId, simpDestination));
        System.out.println(session);
        session.on();
        String holdSession = session.getSessionId();
        headerAccessor.setSessionId(holdSession);
        sessionRepository.save(session);
    }


    public void message() {
    }
}
