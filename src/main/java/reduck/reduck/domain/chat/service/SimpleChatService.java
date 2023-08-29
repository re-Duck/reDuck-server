package reduck.reduck.domain.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.chat.dto.*;
import reduck.reduck.domain.chat.dto.mapper.ChatMessagesResDtoMapper;
import reduck.reduck.domain.chat.dto.mapper.ChatRoomListResDtoMapper;
import reduck.reduck.domain.chat.entity.*;
import reduck.reduck.domain.chat.repository.ChatMessageRepository;
import reduck.reduck.domain.chat.repository.ChatRoomRepository;
import reduck.reduck.domain.chat.repository.ChatRoomUsersRepository;
import reduck.reduck.domain.chat.repository.SessionRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.ChatErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.ChatException;
import reduck.reduck.global.exception.exception.UserException;
import reduck.reduck.util.AuthenticationToken;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.util.Predicates.negate;

@Slf4j
@RequiredArgsConstructor
@Service
public class SimpleChatService extends ChatService {
    private final static int UN_READ_MESSAGE_MAX_SIZE = 300;
    private final static int SHOWABLE_MESSAGE_MAX_SIZE = 20;
    private final static ChatMessage defaultChatMessage = null;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;
    private final SessionRepository sessionRepository;

    @Override
    @Transactional
    public List<ChatRoomListResDto>
    getRooms() {
        // 얘도 paging으로 바꿔야함.

        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
        List<ChatRoomUsers> chatRoomUsers = chatRoomUsersRepository.findAllByUser(user);
        Pageable pageable = PageRequest.of(0, UN_READ_MESSAGE_MAX_SIZE);

        return chatRoomUsers.stream()
                .map(myChatRoomInfo -> {
                    ChatRoom chatRoom = myChatRoomInfo.getRoom();
                    List<ChatMessage> chatMessages = getRecentChatHistory(chatRoom, pageable);
                    if (chatMessages.isEmpty()) return null; // 채팅 신청 후 아무런 메시지도 보낸 기록 없는 경우(방이 비어있음) : 채팅방 안보여줌.
                    List<User> others = getOtherUsersInChatRoom(chatRoom, user);// 채팅 방 별, 나를 제외한 다른 사용자들.
                    Long unreadMessageCount = countOfUnreadMessages(chatRoom, myChatRoomInfo, chatMessages, userId); // 안읽은 메시지 수
                    return ChatRoomListResDtoMapper.of(others, chatRoom, chatMessages.get(0), unreadMessageCount);
                })
                .filter(chatRoomListResDto -> chatRoomListResDto != null) // 대화 내역이 있는 채팅방만.
                .sorted((o1, o2) -> o2.getLastChatMessageTime().compareTo(o1.getLastChatMessageTime())) // 마지막 메시지 시간으로 정렬.
                .collect(Collectors.toList());
    }

    private List<ChatMessage> getRecentChatHistory(ChatRoom chatRoom, Pageable pageable) {

        return chatMessageRepository.findAllByRoomOrderByIdDesc(chatRoom, pageable); // 채팅방 최신 300개 메시지 내역

    }

    private Long countOfUnreadMessages(ChatRoom chatRoom, ChatRoomUsers myChatRoomInfo, List<ChatMessage> chatMessages, String userId) {
        if (chatMessages.get(0).getSender().getUserId().equals(userId)) return 0L;
        if (myChatRoomInfo.getLastChatMessage() == null) {
            // 채팅방이 생성 된 후, 한번도 들어가본적이 없다.
            // 채팅방의 모든 내역들이 안 읽은 메시지다.
            return (long) chatMessages.size();
        }
        Long chatMessageId = myChatRoomInfo.getLastChatMessage().getId(); // 마지막 메시지.
        return chatMessages.stream()
                .filter(negate(chatMessage -> chatMessage.getSender().getUserId().equals(userId))) // 내가 보낸 메시지가 아니고,
                .filter(chatMessage -> chatMessage.getId() > chatMessageId) // 더 오래된 메시지
                .count();
    }

    // 채팅 방 별, 나를 제외한 다른 사용자들.
    private List<User> getOtherUsersInChatRoom(ChatRoom chatRoom, User user) {
        List<ChatRoomUsers> othersChatRoomInfo = chatRoomUsersRepository.findAllByRoomAndUserNot(chatRoom, user);
        return othersChatRoomInfo.stream()
                .map(other -> other.getUser())
                .collect(Collectors.toList());

    }

    //    채팅방 하나 불러오기 paging 사용.
    @Override
    public ChatRoomResDto getRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_EXIST));
        Pageable pageable = PageRequest.of(0, SHOWABLE_MESSAGE_MAX_SIZE);
        List<ChatMessage> chatMessages = getRecentChatHistory(chatRoom, pageable);

        List<ChatMessagesResDto> chatMessagesResDtos = ChatMessagesResDtoMapper.from(chatMessages);

        return ChatRoomResDto.builder()
                .roomId(roomId)
                .chatMessages(chatMessagesResDtos)
                .build();
    }

    //채팅방 생성
    @Override
    @Transactional
    public String createRoom(ChatRoomReqDto chatRoomReqDto) {
        String userId = AuthenticationToken.getUserId();
        // 먼저 생성된 채팅 방이 있으면 그 채팅방 리턴.
        List<String> participantIds = mergeParticipantIds(userId, chatRoomReqDto.getOtherIds());
        return createRoomIfAbsent(chatRoomReqDto, participantIds);
    }

    private List<String> mergeParticipantIds(String userId, List<String> otherIds) {
        ArrayList<String> participantIds = new ArrayList<>();
        participantIds.add(userId);
        for (String id : otherIds) {
            participantIds.add(id);
        }
        return Collections.unmodifiableList(participantIds);
    }

    private String createRoomIfAbsent(ChatRoomReqDto chatRoomReqDto, List<String> participantIds) {
        String alias = createAlias(participantIds);
        Optional<ChatRoom> oldChatRoom = chatRoomRepository.findByAlias(alias);
        return oldChatRoom.isPresent() ?
                oldChatRoom.get().getRoomId() :
                createNewRoom(chatRoomReqDto, participantIds, alias);

    }

    private String createNewRoom(ChatRoomReqDto chatRoomReqDto, List<String> participantIds, String alias) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(chatRoomReqDto.getRoomId())
                .roomName(chatRoomReqDto.getRoomName().isEmpty() ? alias : chatRoomReqDto.getRoomName())
                .alias(alias)
                .build();
        List<ChatRoomUsers> chatRoomUsers = new ArrayList<>();
        for (String id : participantIds) {
            User participant = userRepository.findByUserId(id).get();
            ChatRoomUsers chatRoomUser = ChatRoomUsers.builder()
                    .room(chatRoom)
                    .roomName(chatRoomReqDto.getRoomName().isEmpty() ? alias : chatRoomReqDto.getRoomName())
                    .user(participant)
                    .lastChatMessage(defaultChatMessage)
                    .build();
            chatRoomUsers.add(chatRoomUser);
        }
        chatRoomRepository.save(chatRoom);
        chatRoomUsersRepository.saveAll(chatRoomUsers);
        return chatRoomReqDto.getRoomId();
    }

    //채팅 저장.
    @Override
    @Transactional
    public void sendMessage(ChatMessageReqDto chatMessageReqDto, Message<?> m) {
        Optional<ChatRoom> byRoomId = chatRoomRepository.findByRoomId(chatMessageReqDto.getRoomId());
        User user = userRepository.findByUserId(chatMessageReqDto.getUserId()).get();
        ChatMessage chat = ChatMessage.builder()
                .type(chatMessageReqDto.getType())
                .room(byRoomId.get())
                .sender(user)
                .message(chatMessageReqDto.getMessage())
                .messageId(chatMessageReqDto.getMessageId())
                .build();

        chatMessageRepository.save(chat);
        postSend(m, chatMessageReqDto);
    }


    //    @Override
    private void postSend(Message<?> message, ChatMessageReqDto dto) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String sessionId = headerAccessor.getSessionId();

        // 현재 세션에 해당되는 roomId
        Session mySession = sessionRepository.findBySessionId(sessionId).orElseThrow(() -> new ChatException(ChatErrorCode.SESSION_NOT_EXIST));

        // roomId에 대한 참여 user Id목록을 가져오고,
        ChatRoom room = mySession.getRoom();
        String userId = mySession.getUserId();
        User user = userRepository.findByUserId(userId).get();

        // Send된 message를 json으로 변환 후 messageId 가져옴.
        String messageId = resolveMessage(message);
        ChatMessage chatMessage = chatMessageRepository.findByMessageId(messageId).get(); // 현재 전송 된 메시지.

        List<User> others = getOtherUsersInChatRoom(room, user);// 채팅 방 별, 나를 제외한 다른 사용자들.
        for (User other : others) { // 다른 참여자들의 session 상태에 따라 last_chat_message필드 update.
            Optional<Session> optSession = sessionRepository.findByUserIdAndRoom(other.getUserId(), room);
            if (optSession.isPresent()) {
                Session otherSession = optSession.get();
                updateLastChatMessage(otherSession, other, room, chatMessage);

            }
        }
    }

    private String resolveMessage(Message<?> message) {
        // Session 테이블에 각 room별로 user의 session Id는 고정.
        System.out.println(message.toString());
        byte[] jsonData = (byte[]) message.getPayload(); // 바이트 배열
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resultMap = null;
        try {
            resultMap = objectMapper.readValue(jsonData, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("resultMap = " + resultMap);
        return (String) resultMap.get("messageId");

    }

    private void updateLastChatMessage(Session otherSession, User other, ChatRoom room, ChatMessage chatMessage) {
        SessionStatus otherSessionStatus = otherSession.getStatus();
        if (otherSessionStatus == SessionStatus.ON) {
            // 접속 중 : 상대방의 마지막 읽은 메시지 를 현재 message로 업데이트
            ChatRoomUsers chatRoomInfoOfOther = chatRoomUsersRepository.findByUserAndRoom(other, room);
            chatRoomInfoOfOther.updateLastChatMessage(chatMessage);
            chatRoomUsersRepository.save(chatRoomInfoOfOther);
        }
    }
    /**
     * 채팅방 입장 시 등록됨.
     */
    private String createAlias(List<String> participantIds) {
        Objects.requireNonNull(participantIds);
        return participantIds.stream()
                .sorted()
                .collect(Collectors.joining(","));
    }

}