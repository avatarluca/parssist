package parssist.parser.top_down_analysis.nrdparser.generator.exception;


/**
 * Class, used when there is an exception while generation.
 */
public class GenerationException extends Exception {
    public GenerationException(final String message) {
        super(message);
    }

    public GenerationException() {
        super();
    }
}
