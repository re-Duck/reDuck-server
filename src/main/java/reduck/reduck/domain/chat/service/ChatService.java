package reduck.reduck.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.chat.dto.ChatMessageDto;
import reduck.reduck.domain.chat.dto.ChatRoomDto;
import reduck.reduck.domain.chat.entity.ChatMessage;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.repository.ChatMessageRepository;
import reduck.reduck.domain.chat.repository.ChatRoomRepository;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.CommonException;
import reduck.reduck.global.exception.exception.UserException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

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
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(roomId)
                .orElseThrow(() -> new CommonException(CommonErrorCode.RESOURCE_NOT_FOUND));
        return chatMessages;
    }

    //채팅방 생성
    public void createRoom(ChatRoomDto chatRoomDto) {
        ChatRoom build = ChatRoom.builder()
                .roomId(chatRoomDto.getRoomId())
                .roomName(chatRoomDto.getName())
                .build();
        chatRoomRepository.save(build);

    }

    //채팅 저장.
    public void sendMessage(ChatMessageDto chatMessageDto) {
        ChatMessage chat = ChatMessage.builder()
                .type(chatMessageDto.getType())
                .roomId(chatMessageDto.getRoomId())
                .sender(chatMessageDto.getUserId())
                .message(chatMessageDto.getMessage())
                .build();

        chatMessageRepository.save(chat);
    }
}