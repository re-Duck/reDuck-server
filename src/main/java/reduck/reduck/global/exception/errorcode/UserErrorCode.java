package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치 하지 않습니다."),
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "중복된 사용자 아이디입니다."),
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
