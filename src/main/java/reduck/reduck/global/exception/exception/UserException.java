package reduck.reduck.global.exception.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
public class UserException extends CustomException{

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
