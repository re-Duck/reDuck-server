package reduck.reduck.global.exception.exception;

import lombok.Getter;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
public class CommentException extends CustomException {
    public CommentException(ErrorCode errorCode) {
        super(errorCode);
    }

}
