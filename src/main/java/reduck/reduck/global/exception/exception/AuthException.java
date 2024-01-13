package reduck.reduck.global.exception.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reduck.reduck.global.exception.errorcode.AuthErrorCode;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
public class AuthException extends CustomException {
    private static final ErrorCode defaultErrorCode = AuthErrorCode.NOT_AUTHORIZED;

    public AuthException() {
        super(defaultErrorCode);
    }

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(String handleMessage) {
        super(defaultErrorCode, handleMessage);
    }

    public AuthException(ErrorCode code, String handleMessage) {
        super(code, handleMessage);
    }
}