package reduck.reduck.global.exception.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
public class PostException extends CustomException {
    public PostException(ErrorCode errorCode) {
        super(errorCode);
    }

}
