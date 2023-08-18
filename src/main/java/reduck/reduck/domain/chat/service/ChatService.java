package reduck.reduck.domain.chat.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageHeaderAccessor;
import reduck.reduck.domain.chat.dto.ChatMessageDto;
import reduck.reduck.domain.chat.dto.ChatMessagesResDto;
import reduck.reduck.domain.chat.dto.ChatRoomDto;
import reduck.reduck.domain.chat.dto.ChatRoomListDto;

import java.util.List;

public abstract class ChatService {
    abstract public List<ChatRoomListDto> getRooms();

    //    채팅방 하나 불러오기 paging 사용.
    abstract public List<ChatMessagesResDto> getRoom(String roomId);

    //채팅방 생성
    abstract public String createRoom(ChatRoomDto chatRoomDto);

    //채팅 저장.
    abstract public void sendMessage(ChatMessageDto chatMessageDto);

    abstract public void preSend(Message<?> m, MessageHeaderAccessor accessor, ChatMessageDto dto) ;


}
