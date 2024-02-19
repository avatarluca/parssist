package parssist.parser;

import parssist.parser.util.tree.ParseTreeNode;


/**
 * Main parser method to allow polymorphism while using different parsers.
 */
abstract public class Parser {
    /**
     * Parse the input.
     * @param w Input to parse.
     * @return Parse tree node.
     * @throws Exception If there is an error while parsing.
     */
    abstract public ParseTreeNode parse(final String w) throws Exception;
}
