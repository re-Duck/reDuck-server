package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {
    // session
    SESSION_NOT_EXIST(HttpStatus.BAD_REQUEST, "Session isn't exist"),
    // chat room
    CHAT_ROOM_NOT_EXIST(HttpStatus.BAD_REQUEST, "ChatRoom isn't exist")
    // chat message
    // chat room user
    ,;


    private final HttpStatus httpStatus;
    private final String message;
}
