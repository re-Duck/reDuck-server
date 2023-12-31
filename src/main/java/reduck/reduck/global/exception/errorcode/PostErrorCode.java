package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_EXIST(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

    ;
    private final HttpStatus httpStatus;
    private final String message;

}
