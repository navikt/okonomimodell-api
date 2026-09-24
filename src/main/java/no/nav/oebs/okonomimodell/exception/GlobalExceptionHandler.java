package no.nav.oebs.okonomimodell.exception;

import no.nav.security.token.support.core.exceptions.JwtTokenInvalidClaimException;
import no.nav.security.token.support.core.exceptions.JwtTokenMissingException;
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String STATUS = "status";
    private static final String TIMESTAMP = "timestamp";
    private static final Pattern ISSUER_PATTERN = Pattern.compile("issuer \\[([^,\\]]+)");

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleInvalidJsonException(
            InvalidJsonException ex) {
        LOGGER.error("500 response due to Invalid JSON retrieved from database: {}", ex.getMessage());
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "Invalid JSON retrieved from database");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(STATUS, 500);
        respons.put(TIMESTAMP, LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleJwtTokenMissingException(
            JwtTokenMissingException ex,
            HttpServletRequest request) {
        LOGGER.warn("Auth rejected: status=401 path={} method={} reason={}",
                request.getRequestURI(), request.getMethod(), ex.getMessage());
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "Missing token to access endpoint");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(STATUS, 401);
        respons.put(TIMESTAMP, LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleJwtTokenUnauthorizedException(
            JwtTokenUnauthorizedException ex,
            HttpServletRequest request) {
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "Unauthorized");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(TIMESTAMP, LocalDateTime.now());

        if (ex.getCause() instanceof JwtTokenInvalidClaimException) {
            LOGGER.warn("Auth rejected: status=403 path={} method={} issuer={} reason={}",
                    request.getRequestURI(),
                    request.getMethod(),
                    extractIssuer(ex.getMessage()),
                    ex.getMessage());
            respons.put(STATUS, 403);
            return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.FORBIDDEN);
        }

        LOGGER.warn("Auth rejected: status=401 path={} method={} issuer={} reason={}",
                request.getRequestURI(),
                request.getMethod(),
                extractIssuer(ex.getMessage()),
                ex.getMessage());
        respons.put(STATUS, 401);
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex) {
        LOGGER.error("500 response due to An unexpected error: {}", ex.getMessage());
        LOGGER.error("Stack trace: ", ex);
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "An unexpected error occurred");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(STATUS, 500);
        respons.put(TIMESTAMP, LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        Map<String, Object> respons = new HashMap<>();
        String parameterName = ex.getPropertyName();
        respons.put(ERROR, "Invalid argument provided for parameter: " + parameterName);
        if (parameterName.contains("oppdatertEtter")) {
            respons.put(MESSAGE, "Expected a date in the format YYYY-MM-DD for parameter: " + parameterName);
        } else {
            respons.put(MESSAGE, ex.getMessage());
        }
        respons.put(STATUS, 400);
        respons.put(TIMESTAMP, LocalDateTime.now());
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

    private String extractIssuer(String message) {
        if (message == null) {
            return "unknown";
        }
        Matcher matcher = ISSUER_PATTERN.matcher(message);
        return matcher.find() ? matcher.group(1) : "unknown";
    }

}
