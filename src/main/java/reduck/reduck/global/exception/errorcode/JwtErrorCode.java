package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {

    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "Refresh Token Is Not Exist"),


    ;
    private final HttpStatus httpStatus;
    private final String message;

}
