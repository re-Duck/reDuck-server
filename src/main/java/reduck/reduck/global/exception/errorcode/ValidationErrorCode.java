package reduck.reduck.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ValidationErrorCode implements ErrorCode {
    //ModifyUserDto
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST,"password","VE00001", "Password Must Not Be Null"),
    INVALID_NAME(HttpStatus.BAD_REQUEST,"name", "VE00001","Name Must Not Be Null"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "email","VE00001","Must Be A Valid Format Email Address"),
    INVALID_DEVELOP_YEAR(HttpStatus.BAD_REQUEST,"developYear","VE00001", "Develop Year Must Not Be Null"),

    //SignUpDto
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "userId", "INVALID_USER_ID","Develop Year Must Not Be Null"),

    ;
    private final HttpStatus httpStatus;
    private final String field;
    private final String code;
    private final String message;

    @Override
    public String code() {
        return code;
    }
    public static ValidationErrorCode getCodeFrom(String field){
        ValidationErrorCode validationErrorCode = Arrays.stream(values())
                .filter(value -> value.field.equals(field))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        return validationErrorCode;

    }
}
