package parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.util.tree.visitor;

import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.util.tree.ParseTreeNode;


/**
 * Visitor interface.
 */
public interface Visitor {
    /**
     * Visit the object.
     * @param obj Object to visit.
     */
    public void visit(ParseTreeNode obj);
}