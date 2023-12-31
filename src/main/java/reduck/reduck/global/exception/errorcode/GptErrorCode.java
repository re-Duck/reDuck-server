package reduck.reduck.global.exception.errorcode;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GptErrorCode implements ErrorCode{
    MEMBERSHIP_NOT_JOINED(HttpStatus.FORBIDDEN, "멤버십에 가입 되어 있지 않은 사용자 입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
