package no.nav.oebs.okonomimodell.exception;

import no.nav.security.token.support.core.exceptions.JwtTokenMissingException;
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String STATUS = "status";
    private static final String TIMESTAMP = "timestamp";

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleInvalidJsonException(
            InvalidJsonException ex) {
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "Invalid JSON retrieved from database");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(STATUS, 500);
        respons.put(TIMESTAMP, LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleJwtTokenMissingException(
            JwtTokenMissingException ex) {
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "Missing token to access endpoint");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(STATUS, 401);
        respons.put(TIMESTAMP, LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleJwtTokenUnauthorizedException(
            JwtTokenUnauthorizedException ex) {
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "Unauthorized");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(STATUS, 401);
        respons.put(TIMESTAMP, LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex) {
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "An unexpected error occurred");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(STATUS, 500);
        respons.put(TIMESTAMP, LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
