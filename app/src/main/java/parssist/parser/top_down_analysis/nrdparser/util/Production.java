package parssist.parser.top_down_analysis.nrdparser.util;

import java.util.List;

import parssist.lexer.util.Token;


/**
 * A rule, which is used in {@link Grammar}.
 */
public class Production {
    private final Token lhs;
    private final List<Token[]> rhs;

    
    public Production(Token lhs, List<Token[]> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }


    public Token getLhs() {
        return lhs;
    }

    public List<Token[]> getRhs() {
        return rhs;
    }
}