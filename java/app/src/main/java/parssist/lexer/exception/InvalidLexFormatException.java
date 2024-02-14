package parssist.lexer.exception;


/**
 * InvalidLexFormatException class, used when there is an exception while initialising (scanning) the main lex file.
 */
public class InvalidLexFormatException extends Exception {
    public InvalidLexFormatException(final String message) {
        super(message);
    }

    public InvalidLexFormatException() {
        super();
    }
}