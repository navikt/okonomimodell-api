package no.nav.oebs.okonomimodell.exception;

public class InvalidOebsResponseException extends RuntimeException {
    public InvalidOebsResponseException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidOebsResponseException(String message) {
        super(message);
    }
}
