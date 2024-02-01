package parssist.parser.top_down_analysis.nrdparser.util;

import java.util.List;

import parssist.lexer.util.Token;


/**
 * A rule, which is used in {@link Grammar}.
 */
public class Production {
    private final Token lhs;
    private final List<Token[]> rhs;

    
    public Production(final Token lhs, final List<Token[]> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }


    @Override public boolean equals(final Object obj) {
        if(obj instanceof Production) {
            final Production other = (Production)obj;
            return this.lhs.equals(other.lhs) && isRhsEqual(other.rhs);
        }

        return false;
    }


    public Token getLhs() {
        return lhs;
    }

    public List<Token[]> getRhs() {
        return rhs;
    }


    /**
     * Check if the rhs of this production is equal to the rhs of the other production.
     * Note that the order of the rhs is important.
     * @param otherRhs The rhs of the other production.
     * @return True if the rhs of this production is equal to the rhs of the other production, false otherwise.
     */
    private boolean isRhsEqual(final List<Token[]> otherRhs) {
        if(this.rhs.size() != otherRhs.size()) return false;

        for(int i = 0; i < this.rhs.size() && i < otherRhs.size(); i++) {
            for(int j = 0; j < this.rhs.get(i).length && j < otherRhs.get(i).length; j++) {
                if(!this.rhs.get(i)[j].equals(otherRhs.get(i)[j])) return false;
            }
        }

        return true;
    }
}