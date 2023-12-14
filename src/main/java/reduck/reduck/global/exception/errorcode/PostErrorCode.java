package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_EXIST(HttpStatus.NOT_FOUND, "Post isn't exist"),

    ;
    private final HttpStatus httpStatus;
    private final String message;

}
