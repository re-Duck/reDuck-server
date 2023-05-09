package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "Refresh Token Is Not Exist"),
    AUTHENTICATON_NUMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "Email Authentication Number Is Not Match"),
    EXPIRED_AUTHENTICATION_NUMBER(HttpStatus.BAD_REQUEST, "Authentication Number Is Expired"),
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "You Are Not Authorized")
    ;
    private final HttpStatus httpStatus;
    private final String message;

}
