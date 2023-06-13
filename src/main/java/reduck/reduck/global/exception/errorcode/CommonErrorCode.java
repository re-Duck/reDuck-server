package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import reduck.reduck.global.exception.errorcode.ErrorCode;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST,"INVALID_PARAMETER", "Invalid Parameter Included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,"RESOURCE_NOT_FOUND", "Resource Not Exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR","Internal Server Error"),
    DATA_INTEGRITY_VIOLATION(HttpStatus.BAD_REQUEST, "DATA_INTEGRITY_VIOLATION","DataIntegrityViolationException(무결성제약조건 위반)"),
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public String code() {
        return code;
    }
}
