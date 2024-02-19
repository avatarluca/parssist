package parssist.parser.util.tree;

import java.util.List;

import parssist.Config;
import parssist.lexer.util.Token;
import parssist.parser.util.tree.visitor.Visitor;

import java.util.ArrayList;


/**
 * A Node, which is used as a tree node in a parse tree.
 */
public class ParseTreeNode {
    private final Token token;
    private final List<ParseTreeNode> children;

    private ParseTreeNode parent;


    /**
     * Creates a new node.
     * @param token Token of the node.
     * @param parent Parent of the node.
     * @param children Children of the node.
     */
    public ParseTreeNode(final Token token, final ParseTreeNode parent, final List<ParseTreeNode> children) {
        this.token = token;
        this.parent = parent;
        this.children = children;
    }

    /**
     * Creates a new root node.
     * @param token Token of the node.
     */
    public ParseTreeNode(final Token token) {
        this(token, null, new ArrayList<>());
    }


    public Token getToken() {
        return token;
    }

    public ParseTreeNode getParent() {
        return parent;
    }

    public List<ParseTreeNode> getChildren() {
        return children;
    }

    public void setParent(final ParseTreeNode parent) {
        this.parent = parent;
    }

    
    /**
     * Clean the children of the node.
     */
    public void cleanChildren() {
        children.clear();
    }

    /**
     * Add a child to the node if its not already a child.
     * TODO: This is a hack to avoid adding the same child multiple times. This should be fixed in the future, 
     * by giving the grammar to the parsetree and call its {@link Grammar#isNonTerminal(String)} method to check if the child is a non-terminal.
     * Maybe also check the input token, so that we could also add same terminals multiple times.
     * @param child Child to add.
     */
    public void addChild(final ParseTreeNode child) {
        if(child.getToken().tokenType().name().equals(Config.LEXER_NONTERMINAL) || !children.contains(child)) children.add(child);
    }

    /**
     * Remove a child from the node.
     * @param child Child to remove.
     */
    public void removeChild(final ParseTreeNode child) {
        children.remove(child);
    }

    /**
     * Check if the node is a leaf.
     * @return True if the node is a leaf, false otherwise.
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }


    /**
     * Accept a visitor.
     * @param vis The visitor to accept.
     */
    public void accept(final Visitor vis) {
        vis.visit(this);
    }


    @Override public boolean equals(final Object obj) {
        if(obj instanceof ParseTreeNode) {
            final ParseTreeNode other = (ParseTreeNode) obj;
            return this.token.equals(other.token);
        }

        return false;
    }

    @Override public String toString() {
        return token.toString();
    }
}