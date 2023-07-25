package reduck.reduck.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import reduck.reduck.domain.chat.dto.ChatRoomDto;
import reduck.reduck.domain.chat.entity.ChatMessage;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.service.ChatService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Log4j2
public class RoomController {
    private final ChatService chatService;

    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

// 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatService.findAllRoom();
    }

    //채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<List<ChatMessage>> getRoom(@PathVariable String roomId) {
        return new ResponseEntity(chatService.findById(roomId)
                , HttpStatus.OK);
    }

// 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {

        model.addAttribute("roomId", roomId);

        return "/chat/roomdetail";

    }
    //유저에 대한 채팅방 목록 조회
    @GetMapping(value = "/rooms/{userId}")
    @ResponseBody

    public ResponseEntity<ChatRoom> rooms(@PathVariable String userId) {

        log.info("# All Chat Rooms Ny User : " + userId);

        return new ResponseEntity(chatService.findAllRoom(), HttpStatus.OK);
    }

    //채팅방 개설
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<Void> create(@RequestBody ChatRoomDto chatRoomDto) {

        log.info("# Create Chat Room , roomId: " + chatRoomDto.getRoomId());
        chatService.createRoom(chatRoomDto);
        //        rttr.addFlashAttribute("roomName", repository.createChatRoomDTO(name));

        return new ResponseEntity(HttpStatus.CREATED);
    }


}
