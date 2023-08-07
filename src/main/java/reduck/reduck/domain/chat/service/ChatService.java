package reduck.reduck.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.chat.dto.ChatMessageDto;
import reduck.reduck.domain.chat.dto.ChatRoomDto;
import reduck.reduck.domain.chat.entity.ChatMessage;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.entity.ChatRoomUsers;
import reduck.reduck.domain.chat.repository.ChatMessageRepository;
import reduck.reduck.domain.chat.repository.ChatRoomRepository;
import reduck.reduck.domain.chat.repository.ChatRoomUsersRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.CommonException;
import reduck.reduck.global.exception.exception.UserException;
import reduck.reduck.util.AuthenticationToken;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;

    public List<ChatRoom> findAllRoom() {

        // 채팅방 생성순서 최근 순으로 반환

        List<ChatRoom> rooms = chatRoomRepository.findAll();

        Collections.reverse(rooms);

        return rooms;

    }
//    //채팅방 불러오기
//    public List<ChatRoom> findAllRoom(String userId) {
//        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUserId(userId)
//                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
//
//        return chatRooms;
//    }

    //채팅방 하나 불러오기
    public List<ChatMessage> findById(String roomId) {
        roomId += "#1";
        System.out.println(roomId);
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).get();
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoom(chatRoom)
                .orElseThrow(() -> new CommonException(CommonErrorCode.RESOURCE_NOT_FOUND));
        System.out.println("=========");
        return chatMessages;
    }

    //채팅방 생성
    @Transactional
    public void createRoom(ChatRoomDto chatRoomDto) {
        String userId = AuthenticationToken.getUserId();
        // for dev
        userId = "test1234";
        System.out.println("userId = " + userId);
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));

        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(chatRoomDto.getRoomId())
                .lastChatMessage("")
                .build();

        ChatRoomUsers chatRoomUsers = ChatRoomUsers.builder()
                .room(chatRoom)
                .user(user)
                .build();
        chatRoomRepository.save(chatRoom);
        chatRoomUsersRepository.save(chatRoomUsers);

    }

    //채팅 저장.
    public void sendMessage(ChatMessageDto chatMessageDto) {
        Optional<ChatRoom> byRoomId = chatRoomRepository.findByRoomId(chatMessageDto.getRoomId());
        User user = userRepository.findByUserId(chatMessageDto.getUserId()).get();
        ChatMessage chat = ChatMessage.builder()
                .type(chatMessageDto.getType())
                .room(byRoomId.get())
                .sender(user)
                .message(chatMessageDto.getMessage())
                .build();

        chatMessageRepository.save(chat);
        System.out.println("=======================");
    }

    /**
     * 채팅방 입장 시 등록됨.
     */
    @Transactional
    public void joinUser(ChatMessageDto message) {
        String roomId = message.getRoomId();
        String userId = message.getUserId();
        User user = userRepository.findByUserId(userId).get();
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).get();
        Optional<ChatRoomUsers> byRoomIdAndUserId = chatRoomUsersRepository.findByRoomAndUser(chatRoom, user);
        if (byRoomIdAndUserId.isPresent()) {
            // 이미 존재 = 이미 채팅방에 접속한 적이 있는 사용자.
            System.out.println(" 이미 존재. ");
            return;
        }
        ChatRoomUsers chatRoomUsers = ChatRoomUsers.builder()
                .user(user)
                .room(chatRoom)
                .build();

        chatRoomUsersRepository.save(chatRoomUsers);

    }
}