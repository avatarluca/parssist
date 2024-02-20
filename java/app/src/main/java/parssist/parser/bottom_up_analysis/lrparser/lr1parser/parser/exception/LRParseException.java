package parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.exception;


/**
 * Class, used when there is an exception while parsing the input.
 */
public class LRParseException extends Exception {
    public LRParseException() {
        super();
    }
 
    public LRParseException(String message) {
        super(message);
    }
}