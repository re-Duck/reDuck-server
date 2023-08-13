package reduck.reduck.domain.chat.service;

import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.chat.dto.ChatMessageDto;
import reduck.reduck.domain.chat.dto.ChatMessagesResDto;
import reduck.reduck.domain.chat.dto.ChatRoomDto;
import reduck.reduck.domain.chat.dto.ChatRoomListDto;
import reduck.reduck.domain.chat.entity.ChatMessage;

import java.util.List;

public interface ChatService {
    List<ChatRoomListDto> getRooms();

    //    채팅방 하나 불러오기 paging 사용.
    List<ChatMessagesResDto> getRoom(String roomId);

    //채팅방 생성
    @Transactional
    String createRoom(ChatRoomDto chatRoomDto);

    //채팅 저장.
    void sendMessage(ChatMessageDto chatMessageDto);

    @Transactional
    void joinUser(ChatMessageDto message);
}
