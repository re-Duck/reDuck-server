package reduck.reduck.global.exception.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reduck.reduck.global.exception.errorcode.ErrorCode;

import java.time.LocalDateTime;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    private final String handleMessage;


    public CustomException(ErrorCode code) {
        this.errorCode = code;
        this.handleMessage = null;
    }

    public CustomException(ErrorCode code, String handleMessage) {
        this.handleMessage = handleMessage;
        this.errorCode = code;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "errorCode=" + errorCode +
                ", handleMessage='" + handleMessage + '\'' +
                '}';
    }
}
