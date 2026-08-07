package no.nav.oebs.okonomimodell.exception;

import no.nav.security.token.support.core.exceptions.JwtTokenMissingException;
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleInvalidJsonException_shouldReturn500() {
        var ex = new InvalidJsonException("ugyldig JSON fra DB");

        var response = handler.handleInvalidJsonException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertResponseBody(response.getBody(), 500, "Invalid JSON retrieved from database", "ugyldig JSON fra DB");
    }

    @Test
    void handleJwtTokenMissingException_shouldReturn401() {
        var ex = new JwtTokenMissingException("mangler token");

        var response = handler.handleJwtTokenMissingException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertResponseBody(response.getBody(), 401, "Missing token to access endpoint", "mangler token");
    }

    @Test
    void handleJwtTokenUnauthorizedException_shouldReturn401() {
        var ex = mock(JwtTokenUnauthorizedException.class);
        when(ex.getMessage()).thenReturn("ikke autorisert");

        var response = handler.handleJwtTokenUnauthorizedException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertResponseBody(response.getBody(), 401, "Unauthorized", "ikke autorisert");
    }

    @Test
    void handleGenericException_shouldReturn500() {
        var ex = new RuntimeException("uventet feil");

        var response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertResponseBody(response.getBody(), 500, "An unexpected error occurred", "uventet feil");
    }

    @Test
    void handleTypeMismatch_shouldReturn400() {
        var ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getPropertyName()).thenReturn("system");
        when(ex.getMessage()).thenReturn("Failed to convert value");

        var response = handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertTrue(body.get("error").toString().contains("system"));
    }

    @Test
    void handleTypeMismatch_withOppdatertEtterParam_shouldIncludeDateFormatHint() {
        var ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getPropertyName()).thenReturn("oppdatertEtter");

        var response = handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.get("message").toString().contains("YYYY-MM-DD"));
    }

    @Test
    void allHandlers_shouldIncludeTimestamp() {
        var ex = new InvalidJsonException("feil");

        var response = handler.handleInvalidJsonException(ex);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().get("timestamp"));
    }

    private void assertResponseBody(Map<String, Object> body, int expectedStatus, String expectedError, String expectedMessage) {
        assertNotNull(body);
        assertEquals(expectedStatus, body.get("status"));
        assertEquals(expectedError, body.get("error"));
        assertEquals(expectedMessage, body.get("message"));
        assertNotNull(body.get("timestamp"));
    }
}
