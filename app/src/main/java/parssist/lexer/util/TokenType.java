package parssist.lexer.util;


/**
 * Class, which represent a token type.
 */
public record TokenType(String name, String regex, int priority, boolean ignore) {
    @Override public boolean equals(Object obj) {
        if (obj instanceof TokenType) {
            TokenType other = (TokenType)obj;
            return this.name.equals(other.name);
        }

        return false;
    }
}