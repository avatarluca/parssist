package parssist.lexer.util;


/**
 * Class, which represents a token
 */
public record Token(TokenType tokenType, String symbol) {
    @Override public boolean equals(Object obj) {
        if (obj instanceof Token) {
            Token other = (Token)obj;
            return this.tokenType.equals(other.tokenType) && this.symbol.equals(other.symbol);
        }

        return false;
    }
}