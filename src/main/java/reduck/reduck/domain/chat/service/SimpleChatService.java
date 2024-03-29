package reduck.reduck.domain.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.chat.dto.*;
import reduck.reduck.domain.chat.dto.mapper.ChatMessageResDtoMapper;
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
import reduck.reduck.global.exception.exception.NotFoundException;
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
    public List<ChatRoomListResDto> getRooms(Pageable roomsPageable) {
        // 얘도 paging으로 바꿔야함.

        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_EXIST));
        List<ChatRoomUsers> chatRoomUsers = chatRoomUsersRepository.findAllByUser(user, roomsPageable);
        Pageable pageable = PageRequest.of(0, UN_READ_MESSAGE_MAX_SIZE);

        return chatRoomUsers.stream().map(myChatRoomInfo -> {
                    ChatRoom chatRoom = myChatRoomInfo.getRoom();
                    List<ChatMessage> chatMessages = getRecentChatHistory(chatRoom, pageable);
                    if (chatMessages.isEmpty()) return null; // 채팅 신청 후 아무런 메시지도 보낸 기록 없는 경우(방이 비어있음) : 채팅방 안보여줌.
                    List<User> others = getOtherUsersInChatRoom(chatRoom, user);// 채팅 방 별, 나를 제외한 다른 사용자들.
                    Long unreadMessageCount = countOfUnreadMessages(chatRoom, myChatRoomInfo, chatMessages, userId); // 안읽은 메시지 수
                    return ChatRoomListResDtoMapper.of(others, chatRoom, chatMessages.get(0), unreadMessageCount);
                }).filter(chatRoomListResDto -> chatRoomListResDto != null) // 대화 내역이 있는 채팅방만.
                .sorted((o1, o2) -> o2.getLastChatMessageTime().compareTo(o1.getLastChatMessageTime())) // 마지막 메시지 시간으로 정렬.
                .collect(Collectors.toList());
    }

    private List<ChatMessage> getRecentChatHistory(ChatRoom chatRoom, Pageable pageable) {
        return chatMessageRepository.findAllByRoomOrderByIdDesc(chatRoom, pageable); // 채팅방 최신 300개 메시지 내역

    }

    private Long countOfUnreadMessages(ChatRoom chatRoom, ChatRoomUsers myChatRoomInfo, List<ChatMessage> chatMessages, String userId) {
        // 내가 마지막을 보냄 -> 모든 메시지 읽음.
        if (chatMessages.get(0).getSender().getUserId().equals(userId)) return 0L;

        // 상대방이 보낸 채팅 목록
        List<ChatMessage> chatMessagesNotMe = chatMessages.stream().filter(negate(chatMessage -> chatMessage.getSender().getUserId().equals(userId))).collect(Collectors.toList());

        if (myChatRoomInfo.getLastChatMessage() == null) {
            return (long) chatMessagesNotMe.size();
        }

        Long chatMessageId = myChatRoomInfo.getLastChatMessage().getId(); // 마지막 메시지.
        return chatMessagesNotMe.stream().filter(chatMessage -> chatMessage.getId() > chatMessageId).count();
    }

    // 채팅 방 별, 나를 제외한 다른 사용자들.
    private List<User> getOtherUsersInChatRoom(ChatRoom chatRoom, User user) {
        List<ChatRoomUsers> othersChatRoomInfo = chatRoomUsersRepository.findAllByRoomAndUserNot(chatRoom, user);
        return othersChatRoomInfo.stream().map(other -> other.getUser()).collect(Collectors.toList());

    }

    //    채팅방 하나 불러오기 paging 사용.
    @Override
    @Transactional
    public ChatRoomResDto getRoom(String roomId, Pageable pageable, Optional<String> messageId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).orElseThrow(() -> new NotFoundException(ChatErrorCode.CHAT_ROOM_NOT_EXIST));
//        Pageable pageable = PageRequest.of(0, SHOWABLE_MESSAGE_MAX_SIZE);
        List<ChatMessage> chatMessages;
//        = getRecentChatHistory(chatRoom, pageable);
        if (!messageId.isPresent()) {
            // 첫 조회.
            chatMessages = getRecentChatHistory(chatRoom, pageable);
        } else {
            chatMessages = chatMessageRepository.find2(messageId.get(), pageable, chatRoom);

        }
        List<ChatMessagesResDto> chatMessagesResDtos = ChatMessagesResDtoMapper.from(chatMessages);
        String userId = AuthenticationToken.getUserId();
        updateLastChatMessage(chatRoom, userId, chatMessages);

//        // 세션 ON
//        Session session = sessionRepository.findByUserIdAndRoom(userId, chatRoom).orElseThrow(() -> new ChatException(ChatErrorCode.SESSION_NOT_EXIST));
//        session.on();
//        sessionRepository.save(session);
        return ChatRoomResDto.builder().roomId(roomId).chatMessages(chatMessagesResDtos).build();
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
        return oldChatRoom.isPresent() ? oldChatRoom.get().getRoomId() : createNewRoom(chatRoomReqDto, participantIds, alias);

    }

    private String createNewRoom(ChatRoomReqDto chatRoomReqDto, List<String> participantIds, String alias) {
        ChatRoom chatRoom = ChatRoom.builder().roomId(chatRoomReqDto.getRoomId()).roomName(chatRoomReqDto.getRoomName().isEmpty() ? alias : chatRoomReqDto.getRoomName()).alias(alias).build();
        List<ChatRoomUsers> chatRoomUsers = new ArrayList<>();
        for (String id : participantIds) {
            User participant = userRepository.findByUserId(id).get();
            ChatRoomUsers chatRoomUser = ChatRoomUsers.builder().room(chatRoom).roomName(chatRoomReqDto.getRoomName().isEmpty() ? alias : chatRoomReqDto.getRoomName()).user(participant).lastChatMessage(defaultChatMessage).build();
            chatRoomUsers.add(chatRoomUser);
        }
        chatRoomRepository.save(chatRoom);
        chatRoomUsersRepository.saveAll(chatRoomUsers);
        return chatRoomReqDto.getRoomId();
    }

    //채팅 저장.
    @Override
    @Transactional
    public ChatMessageResDto sendMessage(ChatMessageReqDto chatMessageReqDto, Message<?> payload) {
        Optional<ChatRoom> byRoomId = chatRoomRepository.findByRoomId(chatMessageReqDto.getRoomId());
        User user = userRepository.findByUserId(chatMessageReqDto.getUserId()).get();
        ChatMessage chat = ChatMessage.builder().type(chatMessageReqDto.getType()).room(byRoomId.get()).sender(user).message(chatMessageReqDto.getMessage()).messageId(chatMessageReqDto.getMessageId()).build();

        chatMessageRepository.save(chat);

        postSend(payload);
        return ChatMessageResDtoMapper.from(chat);
    }


    //    @Override
    private void postSend(Message<?> payload) {
        //구취시, sessionId + attribute의 roomId 사용 -> 세션 상태 업데이트해야할 세션 특정.

        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(payload, StompHeaderAccessor.class);
        String sessionId = headerAccessor.getSessionId();
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        String targetRoomId = (String) sessionAttributes.get(sessionId);
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(targetRoomId).get();
        // 현재 세션에 해당되는 roomId
//        Session mySession = sessionRepository.findBySessionId(sessionId).orElseThrow(() -> new ChatException(ChatErrorCode.SESSION_NOT_EXIST));
        Session mySession = sessionRepository.findBySessionIdAndRoom(sessionId, chatRoom).orElseThrow(() -> new NotFoundException(ChatErrorCode.SESSION_NOT_EXIST));

        // roomId에 대한 참여 user Id목록을 가져오고,
        ChatRoom room = mySession.getRoom();
        String userId = mySession.getUserId();
        User user = userRepository.findByUserId(userId).get();

        // Send된 message를 json으로 변환 후 messageId 가져옴.
        String messageId = resolveMessage(payload);
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
        byte[] jsonData = (byte[]) message.getPayload(); // 바이트 배열
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> resultMap = null;
        try {
            resultMap = objectMapper.readValue(jsonData, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (String) resultMap.get("messageId");
    }

    /**
     * 메시지를 전송 후, session 상태에 따른 update
     */
    private void updateLastChatMessage(Session otherSession, User other, ChatRoom room, ChatMessage chatMessage) {
        if (otherSession.isOn()) {
            // 접속 중 : 상대방의 마지막 읽은 메시지 를 현재 message로 업데이트
            ChatRoomUsers chatRoomInfoOfOther = chatRoomUsersRepository.findByUserAndRoom(other, room)
                    .orElseThrow(() -> new NotFoundException("상대방을 찾을 수 없습니다."));
            chatRoomInfoOfOther.updateLastChatMessage(chatMessage);
            chatRoomUsersRepository.save(chatRoomInfoOfOther);
        }
    }

    /**
     * 채팅방 입장 시, update
     */

    private void updateLastChatMessage(ChatRoom chatRoom, String userId, List<ChatMessage> chatMessage) {
        if (chatMessage.isEmpty()) return;
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_EXIST));
        ChatRoomUsers chatRoomUsers = chatRoomUsersRepository.findByRoomAndUser(chatRoom, user)
                .orElseThrow(() -> new NotFoundException("상대방을 찾을 수 없습니다."));
        chatRoomUsers.updateLastChatMessage(chatMessage.get(0));
        chatRoomUsersRepository.save(chatRoomUsers);
    }

    /**
     * 채팅방 입장 시 등록됨.
     */
    private String createAlias(List<String> participantIds) {
        Objects.requireNonNull(participantIds);
        return participantIds.stream().sorted().collect(Collectors.joining(","));
    }

}