package parssist.lexer.util;


/**
 * Class, which represent a token type.
 */
public record TokenType(String name, String regex, int priority, boolean ignore) {}