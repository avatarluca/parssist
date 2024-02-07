package parssist.parser.top_down_analysis.nrdparser.generator.exception;


/**
 * Class, used when there is an exception while parsing the input.
 */
public class ParseException extends Exception {
    public ParseException(final String message) {
        super(message);
    }

    public ParseException() {
        super();
    }
}
