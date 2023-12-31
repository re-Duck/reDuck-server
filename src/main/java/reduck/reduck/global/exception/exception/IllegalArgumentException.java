package reduck.reduck.global.exception.exception;

import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.ErrorCode;

public class IllegalArgumentException extends CustomException {
    private static final ErrorCode defaultErrorCode = CommonErrorCode.ILLEGAL_ARGUMENT;

    public IllegalArgumentException() {
        super(defaultErrorCode);
    }

    public IllegalArgumentException(ErrorCode code) {
        super(code);
    }

    public IllegalArgumentException(String handleMessage) {
        super(defaultErrorCode, handleMessage);
    }

    public IllegalArgumentException(ErrorCode code, String handleMessage) {
        super(code, handleMessage);
    }
}
