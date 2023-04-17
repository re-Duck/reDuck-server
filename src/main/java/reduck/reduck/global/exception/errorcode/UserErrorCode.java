package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid Password"),
    DUPLICATE_USER_ID(HttpStatus.BAD_REQUEST, "Duplicated User Id"),
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "User Is Not Exist"),


    ;
    private final HttpStatus httpStatus;
    private final String message;

}
