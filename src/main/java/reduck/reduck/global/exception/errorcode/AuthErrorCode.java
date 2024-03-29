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
    UNAUTHENTICATED_EMAIL(HttpStatus.BAD_REQUEST, "This Is An Unverified Email."),
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "You Are Not Authorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;

}
