package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid Parameter Included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource Not Exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    DATA_INTEGRITY_VIOLATION(HttpStatus.BAD_REQUEST, "DataIntegrityViolationException(무결성제약조건 위반)"),
    IS_NOT_MATCH(HttpStatus.BAD_REQUEST, "Value Is Not Match")

    ;
    private final HttpStatus httpStatus;
    private final String message;


}
