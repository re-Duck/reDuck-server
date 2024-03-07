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
import reduck.reduck.global.entity.Response;
import reduck.reduck.util.AuthenticationToken;

import java.net.URI;
import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<Response<List<ChatRoomListResDto>>> getRooms(
            @PathVariable String userId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        List<ChatRoomListResDto> result = simpleChatService.getRooms(pageable);
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.OK);
    }

    //    채팅방 조회 = 채팅방 입장
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Response<ChatRoomResDto>> getRoom(
            @PathVariable String roomId,
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam Optional<String> messageId
    ) {
        ChatRoomResDto result = simpleChatService.getRoom(roomId, pageable, messageId);
        return new ResponseEntity<>(Response.successResponse(result), HttpStatus.OK);
    }

    // 채팅방 개설
    // 유저 선택 후 채팅 신청.
    @PostMapping("/room")
    public ResponseEntity<Response<Void>> create(@RequestHeader HttpHeaders headers, @RequestBody ChatRoomReqDto chatRoomReqDto) {
        String redirectUrl = simpleChatService.createRoom(chatRoomReqDto);
        headers.setLocation(URI.create("/chat/room/" + redirectUrl));
        return new ResponseEntity<>(Response.successResponse(), headers, HttpStatus.FOUND);
    }

    @GetMapping("/random")
    public ResponseEntity<Response<List<RecommendUserResDto>>> recommendUsers() {
        List<User> users = repository.findAll();
        List<RecommendUserResDto> recommendUsers = users.stream()
                .filter(user -> !AuthenticationToken.getUserId().equals(user.getUserId()))
                .map(user -> RecommendUserResDtoMapper.from(user))
                .limit(2)
                .collect(Collectors.toList());
        return new ResponseEntity<>(Response.successResponse(recommendUsers), HttpStatus.OK);
    }
}
