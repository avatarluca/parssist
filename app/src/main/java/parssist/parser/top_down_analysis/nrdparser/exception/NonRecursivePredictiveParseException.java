package parssist.parser.top_down_analysis.nrdparser.exception;


/**
 * Class, used when there is an exception while parsing the input.
 */
public class NonRecursivePredictiveParseException extends Exception{
    public NonRecursivePredictiveParseException(final String message) {
        super(message);
    }

    public NonRecursivePredictiveParseException() {
        super();
    }
}
