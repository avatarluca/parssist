package parssist.lexer.exception;


/**
 * InvalidTokenException class, used to represent an invalid token exception.
 */
public class InvalidTokenException extends Exception {
    public InvalidTokenException(final String message) {
        super(message);
    }

    public InvalidTokenException() {
        super();
    }
}