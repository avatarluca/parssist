package parssist.parser.top_down_analysis.nrdparser.util;

import java.util.List;


/**
 * A Node, which is used as a tree node in a parse tree.
 * @param <T> Type of the token.
 */
public class MultiNode<T extends Comparable<T>> {
    private final T token;
    private final MultiNode<T> parent;
    private final List<MultiNode<T>> children;


    /**
     * Creates a new node.
     * @param token Token of the node.
     * @param parent Parent of the node.
     * @param children Children of the node.
     */
    public MultiNode(final T token, final MultiNode<T> parent, final List<MultiNode<T>> children) {
        this.token = token;
        this.parent = parent;
        this.children = children;
    }


    public T getToken() {
        return token;
    }

    public MultiNode<T> getParent() {
        return parent;
    }

    public List<MultiNode<T>> getChildren() {
        return children;
    }
}