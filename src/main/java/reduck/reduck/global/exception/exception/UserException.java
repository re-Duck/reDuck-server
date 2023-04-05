package reduck.reduck.global.exception.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
@RequiredArgsConstructor
public class UserException extends RuntimeException{

    private final ErrorCode errorCode;

}
