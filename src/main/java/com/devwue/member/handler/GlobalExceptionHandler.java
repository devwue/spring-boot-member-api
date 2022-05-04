package com.devwue.member.handler;

import com.devwue.member.exception.AuthException;
import com.devwue.member.exception.DuplicationException;
import com.devwue.member.exception.NotAcceptableException;
import com.devwue.member.exception.NotFoundException;
import com.devwue.member.model.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String getMessage(String code) {
        try {
            return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            log.warn("messageSource exception handler {} - {}", e.getCause(), e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> validationList = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(validationList, validationList.get(0)));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleException(NotFoundException e) {
        log.debug("exception: {}, cause: {} , message: {}", e.getClass(), e.getCause(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(getMessage("notFoundException." + e.getMessage())));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse<?>> handleException(AuthException e) {
        log.debug("exception: {}, cause: {} , message: {}", e.getClass(), e.getCause(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.fail(getMessage("authException." + e.getMessage())));
    }

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<ApiResponse<?>> handleException(DuplicationException e) {
        log.debug("exception: {}, cause: {} , message: {}", e.getClass(), e.getCause(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.fail(getMessage("duplicationException." + e.getMessage())));
    }

    @ExceptionHandler(NotAcceptableException.class)
    public ResponseEntity<ApiResponse<?>> handleException(NotAcceptableException e) {
        log.debug("exception: {}, cause: {} , message: {}", e.getClass(), e.getCause(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ApiResponse.fail(getMessage("notAcceptableException." + e.getMessage())));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleException(RuntimeException e) {
        log.debug("exception: {}, cause: {} , message: {}", e.getClass(), e.getCause(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.fail(e.getMessage()));
    }
}
