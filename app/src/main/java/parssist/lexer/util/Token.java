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


    /**
     * Creating a token with a given tokentype.
     * @param tokenType Token type of the token.
     */
    public Token withTokenType(TokenType tokenType) {
        return new Token(tokenType, symbol());
    }

    /**
     * Creating a token with a given symbol.
     * @param symbol Symbol of the token.
     */
    public Token withSymbol(String symbol) {
        return new Token(tokenType(), symbol);
    }
}