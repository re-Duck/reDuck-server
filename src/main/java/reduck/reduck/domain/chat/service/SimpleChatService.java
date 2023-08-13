package reduck.reduck.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.chat.dto.ChatMessageDto;
import reduck.reduck.domain.chat.dto.ChatMessagesResDto;
import reduck.reduck.domain.chat.dto.ChatRoomDto;
import reduck.reduck.domain.chat.dto.ChatRoomListDto;
import reduck.reduck.domain.chat.dto.mapper.ChatMessagesResDtoMapper;
import reduck.reduck.domain.chat.dto.mapper.ChatRoomListDtoMapper;
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
import java.util.stream.Collectors;

import static org.springframework.data.util.Predicates.negate;

@Slf4j
@RequiredArgsConstructor
@Service
public class SimpleChatService implements ChatService {
    private final static int UN_READ_MESSAGE_MAX_SIZE = 300;
    private final static int SHOWABLE_MESSAGE_MAX_SIZE = 20;
    private final static ChatMessage defaultChatMessage = null;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;

    @Override
    public List<ChatRoomListDto> getRooms() {
        // 얘도 paging으로 바꿔야함.

        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_EXIST));
        List<ChatRoomUsers> chatRoomUsers = chatRoomUsersRepository.findAllByUser(user);
        Pageable pageable = PageRequest.of(0, UN_READ_MESSAGE_MAX_SIZE);

        return chatRoomUsers.stream()
                .map(myChatRoomInfo -> {
                    if (myChatRoomInfo.isEmpty()) return null; // 채팅 신청 후 아무런 메시지도 보낸 기록 없는 경우(방이 비어있음) : 채팅방 안보여줌.
                    ChatRoom chatRoom = myChatRoomInfo.getRoom();
                    List<User> others = getOtherUsersInChatRoom(chatRoom, user);// 채팅 방 별, 나를 제외한 다른 사용자들.
                    List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomOrderByIdDesc(chatRoom, pageable).get(); // 채팅방 최신 300개 메시지 내역
                    Long unreadMessageCount = countOfUnreadMessages(myChatRoomInfo, chatMessages, userId); // 안읽은 메시지 수
                    return ChatRoomListDtoMapper.of(others, chatRoom, chatMessages.get(0), unreadMessageCount);
                })
                .filter(chatRoomListDto -> chatRoomListDto != null) // 대화 내역이 있는 채팅방만.
                .sorted((o1, o2) -> o2.getLastChatMessageTime().compareTo(o1.getLastChatMessageTime())) // 마지막 메시지 시간으로 정렬.
                .collect(Collectors.toList());
    }

    private Long countOfUnreadMessages(ChatRoomUsers myChatRoomInfo, List<ChatMessage> chatMessages, String userId) {
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
    public List<ChatMessagesResDto> getRoom(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId).get();
        Pageable pageable = PageRequest.of(0, UN_READ_MESSAGE_MAX_SIZE);
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomOrderByIdDesc(chatRoom, pageable)
                .orElseThrow(() -> new CommonException(CommonErrorCode.RESOURCE_NOT_FOUND));

        return ChatMessagesResDtoMapper.from(chatMessages);

    }

    //채팅방 생성
    @Override
    @Transactional
    public String createRoom(ChatRoomDto chatRoomDto) {
        String userId = AuthenticationToken.getUserId();
        String roomId = chatRoomDto.getRoomId();
        // 먼저 생성된 채팅 방이 있으면 그 채팅방 리턴.
        List<String> participantIds = mergeParticipantIds(userId, chatRoomDto.getOtherIds());
        return createRoomIfAbsent(roomId, participantIds);
    }

    private List<String> mergeParticipantIds(String userId, List<String> otherIds) {
        ArrayList<String> participantIds = new ArrayList<>();
        participantIds.add(userId);
        for (String id : otherIds) {
            participantIds.add(id);
        }
        return Collections.unmodifiableList(participantIds);
    }

    private String createRoomIfAbsent(String roomId, List<String> participantIds) {
        String alias = createAlias(participantIds);
        Optional<ChatRoom> oldChatRoom = chatRoomRepository.findByAlias(alias);
        return oldChatRoom.isPresent() ?
                oldChatRoom.get().getRoomId() :
                createNewRoom(roomId, participantIds, alias);

    }

    private String createNewRoom(String roomId, List<String> participantIds, String alias) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(roomId)
                .alias(alias)
                .build();
        List<ChatRoomUsers> chatRoomUsers = new ArrayList<>();
        for (String id : participantIds) {
            User participant = userRepository.findByUserId(id).get();
            ChatRoomUsers chatRoomUser = ChatRoomUsers.builder()
                    .room(chatRoom)
                    .user(participant)
                    .lastChatMessage(defaultChatMessage)
                    .build();
            chatRoomUsers.add(chatRoomUser);
        }
        chatRoomRepository.save(chatRoom);
        chatRoomUsersRepository.saveAll(chatRoomUsers);
        return roomId;
    }

    //채팅 저장.
    @Override
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
    }

    /**
     * 채팅방 입장 시 등록됨.
     */
    @Override
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

    private String createAlias(List<String> participantIds) {
        Objects.requireNonNull(participantIds);
        return participantIds.stream()
                .sorted()
                .collect(Collectors.joining(","));
    }

}