package reduck.reduck.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import reduck.reduck.domain.chat.dto.ChatRoomDto;
import reduck.reduck.domain.chat.dto.ChatRoomListDto;
import reduck.reduck.domain.chat.dto.RecommendUserDto;
import reduck.reduck.domain.chat.dto.mapper.RecommendUserDtoMapper;
import reduck.reduck.domain.chat.entity.ChatMessage;
import reduck.reduck.domain.chat.entity.ChatRoom;
import reduck.reduck.domain.chat.service.ChatService;
import reduck.reduck.domain.user.dto.UserInfoDtoRes;
import reduck.reduck.domain.user.dto.mapper.UserInfoDtoResMapper;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.util.AuthenticationToken;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Log4j2
public class RoomController {
    private final ChatService chatService;
    private final UserRepository repository;

//    채팅방 조회
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ChatMessage>> getRoom(@PathVariable String roomId) {
        return new ResponseEntity(chatService.getRoom(roomId)
                , HttpStatus.OK);
    }


    //유저에 대한 채팅방 목록 조회
    @GetMapping(value = "/rooms/{userId}")
    public ResponseEntity<List<ChatRoomListDto>> getRooms(@PathVariable String userId) {

        log.info("# All Chat Rooms By User : " + userId);

        return new ResponseEntity(chatService.getRooms(), HttpStatus.OK);
    }

    // 채팅방 개설
    // 유저 선택 후 채팅 신청.
    @PostMapping("/room")
    public ResponseEntity<Void> create(@RequestHeader HttpHeaders headers, @RequestBody ChatRoomDto chatRoomDto) {

        log.info("# Create Chat Room , roomId: " + chatRoomDto.getRoomId());
        String redirectUrl = chatService.createRoom(chatRoomDto);
        headers.setLocation(URI.create("/chat/room/" + redirectUrl));
        return new ResponseEntity(headers, HttpStatus.FOUND);
    }

    @GetMapping("/random")
    public ResponseEntity<List<RecommendUserDto>> recommendUsers() {
        List<User> users = repository.findAll();
        List<RecommendUserDto> recommendUsers = users.stream()
                .filter(user -> !AuthenticationToken.getUserId().equals(user.getUserId()))
                .map(user -> RecommendUserDtoMapper.from(user))
                .limit(2)
                .collect(Collectors.toList());
        return new ResponseEntity<>(recommendUsers, HttpStatus.OK);
    }


}
