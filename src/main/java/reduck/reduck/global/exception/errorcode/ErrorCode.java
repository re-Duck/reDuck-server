package reduck.reduck.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    String code();
    HttpStatus getHttpStatus();
    String getMessage();
}
