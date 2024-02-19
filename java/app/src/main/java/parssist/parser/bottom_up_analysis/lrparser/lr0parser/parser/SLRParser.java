package parssist.parser.bottom_up_analysis.lrparser.lr0parser.parser;

import parssist.parser.util.Grammar;
import parssist.parser.util.Production;

import java.util.List;


/**
 * A simple LR parser implementation, which can create a SLR parsetable.
 */
public class SLRParser extends LRParser {
    private final Grammar grammar;


    /**
     * Creates a new SLR parser.
     */
    public SLRParser(final Grammar grammar) {
        this.grammar = grammar;
    }


    @Override protected List<Production>[][] createParseTable() {
        return null;
    }
}
