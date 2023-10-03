package reduck.reduck.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reduck.reduck.domain.chat.dto.*;
import reduck.reduck.domain.chat.dto.mapper.RecommendUserResDtoMapper;
import reduck.reduck.domain.chat.service.SimpleChatService;
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
    private final SimpleChatService simpleChatService;
    private final UserRepository repository;


    //유저에 대한 채팅방 목록 조회
    @GetMapping(value = "/rooms/{userId}")
    public ResponseEntity<List<ChatRoomListResDto>> getRooms(@PathVariable String userId,
                                                             @PageableDefault(size = 20) Pageable pageable) {

        log.info("# All Chat Rooms By User : " + userId);

        return new ResponseEntity(simpleChatService.getRooms(pageable), HttpStatus.OK);
    }

    //    채팅방 조회 = 채팅방 입장
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatRoomResDto> getRoom(@PathVariable String roomId,
                                                  @PageableDefault(size = 20) Pageable pageable) {
        log.info("# enter chat room By id : " + roomId);
        return new ResponseEntity(simpleChatService.getRoom(roomId, pageable)
                , HttpStatus.OK);
    }

    // 채팅방 개설
    // 유저 선택 후 채팅 신청.
    @PostMapping("/room")
    public ResponseEntity<Void> create(@RequestHeader HttpHeaders headers, @RequestBody ChatRoomReqDto chatRoomReqDto) {

        log.info("# Create Chat Room , roomId: " + chatRoomReqDto.getRoomId());
        String redirectUrl = simpleChatService.createRoom(chatRoomReqDto);
        headers.setLocation(URI.create("/chat/room/" + redirectUrl));
        return new ResponseEntity(headers, HttpStatus.FOUND);
    }

    @GetMapping("/random")
    public ResponseEntity<List<RecommendUserResDto>> recommendUsers() {
        List<User> users = repository.findAll();
        List<RecommendUserResDto> recommendUsers = users.stream()
                .filter(user -> !AuthenticationToken.getUserId().equals(user.getUserId()))
                .map(user -> RecommendUserResDtoMapper.from(user))
                .limit(2)
                .collect(Collectors.toList());
        return new ResponseEntity<>(recommendUsers, HttpStatus.OK);
    }


}
