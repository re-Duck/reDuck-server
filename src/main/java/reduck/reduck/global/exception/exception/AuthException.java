package reduck.reduck.global.exception.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
public class AuthException extends CustomException{
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}