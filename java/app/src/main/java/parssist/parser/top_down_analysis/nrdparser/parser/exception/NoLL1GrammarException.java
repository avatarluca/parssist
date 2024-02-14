package parssist.parser.top_down_analysis.nrdparser.parser.exception;


/**
 * Exception for no LL(1) grammar.
 */
public class NoLL1GrammarException extends Exception {
    public NoLL1GrammarException() {
        super();
    }

    public NoLL1GrammarException(String message) {
        super(message);
    }
}