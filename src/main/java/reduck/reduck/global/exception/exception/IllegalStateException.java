package reduck.reduck.global.exception.exception;

import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.ErrorCode;

public class IllegalStateException extends CustomException {
    private static final ErrorCode defaultErrorCode = CommonErrorCode.ILLEGAL_STATE;

    public IllegalStateException(ErrorCode code) {
        super(code);
    }

    public IllegalStateException(String handleMessage) {
        super(defaultErrorCode, handleMessage);
    }

    public IllegalStateException(ErrorCode code, String handleMessage) {
        super(code, handleMessage);
    }
}
