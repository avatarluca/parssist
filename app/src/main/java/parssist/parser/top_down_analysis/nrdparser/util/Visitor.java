package parssist.parser.top_down_analysis.nrdparser.util;


/**
 * Visitor interface.
 * @param <T> Type of the object to visit.
 */
public interface Visitor<T> {
    /**
     * Visit the object.
     * @param obj Object to visit.
     */
    public void visit(T obj);
}