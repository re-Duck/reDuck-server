package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    COMMENT_NOT_EXIST(HttpStatus.BAD_REQUEST, "Comment isn't exist"),

    ;
    private final HttpStatus httpStatus;
    private final String message;

}
