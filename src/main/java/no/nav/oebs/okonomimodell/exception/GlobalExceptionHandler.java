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

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleInvalidJsonException(
            InvalidJsonException ex) {
        Map<String, Object> respons = new HashMap<>();
        respons.put("error", "Invalid JSON retrieved from database");
        respons.put("message", ex.getMessage());
        respons.put("status", 500);
        respons.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleJwtTokenMissingException(
            JwtTokenMissingException ex) {
        Map<String, Object> respons = new HashMap<>();
        respons.put("error", "Missing token to access endpoint");
        respons.put("message", ex.getMessage());
        respons.put("status", 401);
        respons.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleJwtTokenUnauthorizedException(
            JwtTokenUnauthorizedException ex) {
        Map<String, Object> respons = new HashMap<>();
        respons.put("error", "Unauthorized");
        respons.put("message", ex.getMessage());
        respons.put("status", 401);
        respons.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex) {
        Map<String, Object> respons = new HashMap<>();
        respons.put("error", "An unexpected error occurred");
        respons.put("message", ex.getMessage());
        respons.put("status", 500);
        respons.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
