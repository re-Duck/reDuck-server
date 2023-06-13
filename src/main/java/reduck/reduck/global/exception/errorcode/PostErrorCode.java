package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_EXIST(HttpStatus.BAD_REQUEST, "POST_NOT_EXIST","Post isn't exist"),

    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String code() {
        return code;
    }
}
