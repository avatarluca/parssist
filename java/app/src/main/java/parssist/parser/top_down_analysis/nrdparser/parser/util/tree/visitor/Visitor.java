package parssist.parser.top_down_analysis.nrdparser.parser.util.tree.visitor;

import parssist.parser.top_down_analysis.nrdparser.parser.util.tree.ParseTreeNode;


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