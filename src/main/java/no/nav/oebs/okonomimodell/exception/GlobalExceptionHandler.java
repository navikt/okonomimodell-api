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
    private static final String CORRELATION_ID_FIELD = "correlationId";
    private static final String CORRELATION_ID_HEADER = "x-correlation-id";
    private static final String SYSTEM = "system";
    private static final Pattern ISSUER_PATTERN = Pattern.compile("issuer \\[([^,\\]]+)");

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleInvalidJsonException(
            InvalidJsonException ex,
            HttpServletRequest request) {
        LOGGER.error(
                "500 response due to Invalid JSON retrieved from database: correlationId={} system={} path={} method={} reason={}",
                getCorrelationId(request),
                getSystem(request),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage());
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "Invalid JSON retrieved from database");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(STATUS, 500);
        respons.put(TIMESTAMP, LocalDateTime.now());
        addContextFields(respons, request);
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleJwtTokenMissingException(
            JwtTokenMissingException ex,
            HttpServletRequest request) {
        LOGGER.warn("Auth rejected: status=401 correlationId={} system={} path={} method={} reason={}",
                getCorrelationId(request), getSystem(request), request.getRequestURI(), request.getMethod(), ex.getMessage());
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "Missing token to access endpoint");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(STATUS, 401);
        respons.put(TIMESTAMP, LocalDateTime.now());
        addContextFields(respons, request);
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
        addContextFields(respons, request);

        if (ex.getCause() instanceof JwtTokenInvalidClaimException) {
            LOGGER.warn("Auth rejected: status=403 correlationId={} system={} path={} method={} issuer={} reason={}",
                    getCorrelationId(request),
                    getSystem(request),
                    request.getRequestURI(),
                    request.getMethod(),
                    extractIssuer(ex.getMessage()),
                    ex.getMessage());
            respons.put(STATUS, 403);
            return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.FORBIDDEN);
        }

        LOGGER.warn("Auth rejected: status=401 correlationId={} system={} path={} method={} issuer={} reason={}",
                getCorrelationId(request),
                getSystem(request),
                request.getRequestURI(),
                request.getMethod(),
                extractIssuer(ex.getMessage()),
                ex.getMessage());
        respons.put(STATUS, 401);
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        LOGGER.error(
                "500 response due to An unexpected error: correlationId={} system={} path={} method={} reason={}",
                getCorrelationId(request),
                getSystem(request),
                request.getRequestURI(),
                request.getMethod(),
                ex.getMessage(),
                ex);
        Map<String, Object> respons = new HashMap<>();
        respons.put(ERROR, "An unexpected error occurred");
        respons.put(MESSAGE, ex.getMessage());
        respons.put(STATUS, 500);
        respons.put(TIMESTAMP, LocalDateTime.now());
        addContextFields(respons, request);
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        LOGGER.warn(
                "400 response due to type mismatch: correlationId={} system={} path={} method={} parameter={} reason={}",
                getCorrelationId(request),
                getSystem(request),
                request.getRequestURI(),
                request.getMethod(),
                ex.getPropertyName(),
                ex.getMessage());
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
        addContextFields(respons, request);
        return new ResponseEntity<>(respons, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

    private void addContextFields(Map<String, Object> response, HttpServletRequest request) {
        response.put(CORRELATION_ID_FIELD, getCorrelationId(request));
        response.put(SYSTEM, getSystem(request));
    }

    private String getCorrelationId(HttpServletRequest request) {
        return normalize(request.getHeader(CORRELATION_ID_HEADER));
    }

    private String getSystem(HttpServletRequest request) {
        if (request.getParameter(SYSTEM) != null) {
            return request.getParameter(SYSTEM);
        }
        return null;
    }

    private String extractIssuer(String message) {
        if (message == null) {
            return "unknown";
        }
        Matcher matcher = ISSUER_PATTERN.matcher(message);
        return matcher.find() ? matcher.group(1) : "unknown";
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

}
