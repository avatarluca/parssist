package parssist.parser.top_down_analysis.nrdparser.util;


/**
 * Traversal ADT interface. 
 * @param <T> Type of the object to traverse.
 */
public interface Traversal<T> {
    /**
     * Visit the object.
     * @param <T> Type of the object to visit.
     */
    public void preorder(Visitor<T> visitor);
    
    /**
     * Visit the object.
     * @param <T> Type of the object to visit.
     */
    public void inorder(Visitor<T> visitor);
    
    /**
     * Visit the object.
     * @param <T> Type of the object to visit.
     */
    public void postorder(Visitor<T> visitor);
    
    /**
     * Visit the object.
     * @param <T> Type of the object to visit.
     */
    public void levelorder(Visitor<T> visitor);
   
    /**
     * Visit the object.
     * @param <T> Type of the object to visit.
     */
    public void interval(T min, T max, Visitor<T> visitor);
}