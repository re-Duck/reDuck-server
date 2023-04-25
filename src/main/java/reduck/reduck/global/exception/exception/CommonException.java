package reduck.reduck.global.exception.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
public class CommonException extends CustomException{

    public CommonException(ErrorCode errorCode) {
        super(errorCode);
    }
}