package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "TOKEN_NOT_EXIST","Refresh Token Is Not Exist"),
    AUTHENTICATON_NUMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "AUTHENTICATON_NUMBER_NOT_MATCH", "Email Authentication Number Is Not Match"),
    EXPIRED_AUTHENTICATION_NUMBER(HttpStatus.BAD_REQUEST, "EXPIRED_AUTHENTICATION_NUMBER","Authentication Number Is Expired"),
    UNAUTHENTICATED_EMAIL(HttpStatus.BAD_REQUEST,"UNAUTHENTICATED_EMAIL", "This Is An Unverified Email."),
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "NOT_AUTHORIZED","You Are Not Authorized")
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String code() {
        return code;
    }
}
