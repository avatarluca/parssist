package parssist.parser.top_down_analysis.nrdparser.util;

import java.util.List;


/**
 * A BinaryNode, which is used as a tree node in a parse tree.
 * @param <T> Type of the token.
 */
public class BinaryNode<T extends Comparable<T>> {
    private final T token;
    private final BinaryNode<T> parent;
    private BinaryNode<T> left;
    private BinaryNode<T> right;


    /**
     * Creates a new node.
     * @param token Token of the node.
     * @param parent Parent of the node.
     * @param children Children of the node.
     */
    public BinaryNode(final T token, final BinaryNode<T> parent, final BinaryNode<T> left, final BinaryNode<T> right) {
        this.token = token;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }


    public T getToken() {
        return token;
    }

    public BinaryNode<T> getParent() {
        return parent;
    }

    public BinaryNode<T> getLeft() {
        return left;
    }

    public BinaryNode<T> getRight() {
        return right;
    }
}