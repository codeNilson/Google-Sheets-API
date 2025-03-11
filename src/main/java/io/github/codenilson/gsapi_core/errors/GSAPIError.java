package io.github.codenilson.gsapi_core.errors;

/**
 * Represents an error that occurred while using the GSAPI.
 */
public class GSAPIError extends RuntimeException {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new GSAPIError with the specified message.
     * 
     * @param message The error message.
     */
    public GSAPIError(String message) {
        super(message);
    }

    /**
     * Creates a new GSAPIError with the specified message and cause.
     * 
     * @param message The error message.
     * @param cause   The cause of the error.
     */
    public GSAPIError(String message, Throwable cause) {
        super(message, cause);
    }

}
