package reduck.reduck.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.validation.BindException;
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.ErrorCode;
import reduck.reduck.global.exception.exception.*;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.IllegalArgumentException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private List<FieldError> fieldErrors;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        log.error(e.toString());
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode, e.getHandleMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.error(e.toString());
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode, e.getHandleMessage());
    }

//    @Override
//    public ResponseEntity<Object> handleMethodArgumentNotValid(
//            final MethodArgumentNotValidException e,
//            final HttpHeaders headers,
//            final HttpStatus status,
//            final WebRequest request) {
//        log.warn("handleIllegalArgument", e);
//        fieldErrors = e.getBindingResult().getFieldErrors();
//        System.out.println("e.getBindingResult().getFieldErrors() = " + fieldErrors.get(0).getField());
//        System.out.println("e.getBindingResult().getFieldErrors() = " + fieldErrors.get(0).getRejectedValue());
//        System.out.println("e.getBindingResult().getFieldErrors() = " + fieldErrors.get(0).getDefaultMessage());
//        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
//        return handleExceptionInternal(e, errorCode);
//    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);
        if (ex instanceof DataIntegrityViolationException) {
            ErrorCode errorCode = CommonErrorCode.DATA_INTEGRITY_VIOLATION;
            return handleExceptionInternal(errorCode);
        }
        if (ex instanceof MailSendException) {
            ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
            return handleExceptionInternal(errorCode);
        }
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .status(errorCode.getHttpStatus().value())
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String handleMessage) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, handleMessage));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String handleMessage) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(handleMessage != null ? handleMessage : errorCode.getMessage())
                .status(errorCode.getHttpStatus().value())
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }
}
