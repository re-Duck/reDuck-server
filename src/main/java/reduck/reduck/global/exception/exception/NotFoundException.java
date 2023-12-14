package reduck.reduck.global.exception.exception;

import reduck.reduck.global.exception.errorcode.ErrorCode;


public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode code) {
        super(code, "");
    }

    public NotFoundException(ErrorCode errorCode, String Param) {
        super(errorCode, Param);
    }
}
