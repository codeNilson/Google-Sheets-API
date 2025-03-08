package io.github.codenilson.gsapi_core.errors;

public class GSAPIError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GSAPIError(String message) {
        super(message);
    }

    public GSAPIError(String message, Throwable cause) {
        super(message, cause);
    }

}
