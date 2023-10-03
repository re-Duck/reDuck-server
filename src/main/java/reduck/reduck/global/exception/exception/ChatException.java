package reduck.reduck.global.exception.exception;

import lombok.Getter;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
public class ChatException extends CustomException{
    public ChatException(ErrorCode errorCode) {
        super(errorCode);
    }
}
