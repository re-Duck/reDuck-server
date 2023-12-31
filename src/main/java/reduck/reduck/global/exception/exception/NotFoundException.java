package reduck.reduck.global.exception.exception;

import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.ErrorCode;



public class NotFoundException extends CustomException {
    private static final ErrorCode defaultErrorCode = CommonErrorCode.RESOURCE_NOT_FOUND;

    public NotFoundException() {
        super(defaultErrorCode);
    }

    public NotFoundException(String handleMessage) {
        super(defaultErrorCode, handleMessage);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, String handleMessage) {
        super(errorCode, handleMessage);
    }
}
