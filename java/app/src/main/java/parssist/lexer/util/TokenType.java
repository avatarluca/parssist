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


    /**
     * Creating a token type with a given priority.
     * @param priority Name of the token type.
     */
    public TokenType withPriority(int priority) {
        return new TokenType(name(), regex(), priority, ignore());
    }

    /**
     * Creating a token type with a given ignore.
     * @param ignore If the token type should be ignored.
     */
    public TokenType withIgnore(boolean ignore) {
        return new TokenType(name(), regex(), priority(), ignore);
    }

    /**
     * Creating a token type with a given regex.
     * @param regex Regex of the token type.
     */
    public TokenType withRegex(String regex) {
        return new TokenType(name(), regex, priority(), ignore());
    }
}