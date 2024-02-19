package parssist.parser.util;

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

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(lhs.symbol()).append(" -> ");

        for(int i = 0; i < rhs.size(); i++) {
            for(int j = 0; j < rhs.get(i).length; j++) {
                sb.append(rhs.get(i)[j].symbol());
            }
            sb.append(" ");
            if(i < rhs.size() - 1) sb.append("| ");
        }

        return sb.toString();
    }


    public Token getLhs() {
        return lhs;
    }

    public List<Token[]> getRhs() {
        return rhs;
    }


    /**
     * Check if the rhs of this production contains the empty symbol.
     * @return True if the rhs of this production contains the empty symbol, false otherwise.
     */
    public boolean hasEmptySymbol() {
        for(final Token[] tokens : rhs) {
            for(final Token token : tokens) {
                if(token.symbol().equals(Grammar.EMPTY_SYMBOL)) return true;
            }
        }

        return false;
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