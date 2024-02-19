package parssist.parser.bottom_up_analysis.lrparser.lr0parser.parser;

import java.util.List;

import parssist.parser.Parser;
import parssist.parser.util.Production;
import parssist.parser.util.tree.ParseTreeNode;


/**
 * A non recursive shift-reduce parser.
 */
abstract public class LRParser extends Parser {


    


    @Override
    public ParseTreeNode parse(String w) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parse'");
    }
    

    /**
     * Create a parsetable for the given grammar.
     */
    abstract protected List<Production>[][] createParseTable();
}
