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
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "요청이 잘못 되었습니다. 매개 변수를 확인하세요."),
    DUPLICATE_ARGUMENT(HttpStatus.CONFLICT, "중복된 대상이 있습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    DATA_INTEGRITY_VIOLATION(HttpStatus.BAD_REQUEST, "DataIntegrityViolationException(무결성제약조건 위반)"),
    ILLEGAL_STATE(HttpStatus.UNPROCESSABLE_ENTITY, "요청을 수행 할 수 없습니다." );
    private final HttpStatus httpStatus;
    private final String message;
}
