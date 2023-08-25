package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ErrorCode {
    // chat room
    CHAT_ROOM_NOT_EXIST(HttpStatus.BAD_REQUEST, "Comment isn't exist")
    // chat message
    // chat room user
    ,;


    private final HttpStatus httpStatus;
    private final String message;
}
