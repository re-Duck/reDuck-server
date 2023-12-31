package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GptMembershipErrorCode implements ErrorCode {
    FORBIDDEN_MEMBERSHIP(HttpStatus.FORBIDDEN, "멤버십에 가입되어 있지 않습니다.");

    GptMembershipErrorCode(HttpStatus httpStatus, String s) {
        this.httpStatus = httpStatus;
        this.message = s;
    }

    private final HttpStatus httpStatus;
    private final String message;

}
