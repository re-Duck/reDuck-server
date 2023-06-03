package reduck.reduck.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import reduck.reduck.domain.chat.dto.ChatRoomDto;
import reduck.reduck.domain.chat.entity.ChatMessage;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.service.ChatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/chat")
@Log4j2
public class RoomController {
    private final ChatService chatService;
    @GetMapping
    public ModelAndView chat(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/chat/room");
        return mv;
    }
    //채팅방 목록 조회
    @GetMapping(value = "/rooms/{userId}")
    public ResponseEntity<ChatRoom> rooms(@PathVariable String userId) {

        log.info("# All Chat Rooms Ny User : " + userId);

        return new ResponseEntity(chatService.findAllRoom(userId), HttpStatus.OK);
    }

    //채팅방 개설
    @PostMapping(value = "/room")
    public ResponseEntity<Void> create(@RequestBody ChatRoomDto chatRoomDto) {

        log.info("# Create Chat Room , roomId: " + chatRoomDto.getRoomId());
        chatService.createRoom(chatRoomDto);
        //        rttr.addFlashAttribute("roomName", repository.createChatRoomDTO(name));

        return new ResponseEntity(HttpStatus.CREATED);
    }

    //채팅방 조회
    @GetMapping("/room")
    public ResponseEntity<List<ChatMessage>> getRoom(String roomId) {
        return new ResponseEntity(chatService.findById(roomId)
                , HttpStatus.OK);
    }
}
