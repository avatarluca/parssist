package parssist.parser;


/**
 * Class, which generates a parser.
 */
abstract public class ParserGenerator {
    /**
     * Generate a parser.
     * @param parserName Name of the parser.
     * @param packageName Name of the package.
     * @return Sourcecode.
     */
    abstract public String generate(final String parserName, final String packageName);
}
