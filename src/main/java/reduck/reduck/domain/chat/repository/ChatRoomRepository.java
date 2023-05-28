package reduck.reduck.domain.chat.repository;

import org.springframework.stereotype.Repository;
import reduck.reduck.domain.chat.dto.ChatRoomDto;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {
    private Map<String, ChatRoomDto> chatRoomDTOMap;

    @PostConstruct
    private void init(){
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    public List<ChatRoomDto> findAllRooms(){
        //채팅방 생성 순서 최근 순으로 반환
        List<ChatRoomDto> result = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(result);

        return result;
    }

    public ChatRoomDto findRoomById(String id){
        return chatRoomDTOMap.get(id);
    }

    public ChatRoomDto createChatRoomDTO(String name){
        ChatRoomDto room = ChatRoomDto.create(name);
        chatRoomDTOMap.put(room.getRoomId(), room);

        return room;
    }
}
