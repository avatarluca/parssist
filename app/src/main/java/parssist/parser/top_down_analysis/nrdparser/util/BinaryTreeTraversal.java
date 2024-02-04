package parssist.parser.top_down_analysis.nrdparser.util;

import java.util.*;


/**
 * BinaryTreeTraversal ADT class.
 * @param <T> Type of the object to traverse.
 */
public class BinaryTreeTraversal<T extends Comparable<T>> implements Traversal<T> {
	private final BinaryNode<T> root;


    /**
     * Creates a new tree traversal.
     * @param root Root of the tree.
     */
	public BinaryTreeTraversal(BinaryNode<T> root) {
		this.root = root;
	}


	@Override public void inorder(Visitor<T> vis) {
		inorder(root, vis);
	}

	@Override public void preorder(Visitor<T> vis) {
		preorder(root, vis);
	}

	@Override public void postorder(Visitor<T> vis) {
		postorder(root, vis);
	}

	@Override public void levelorder(Visitor<T> vis) {
		levelorder(root, vis);
	}

	@Override public void interval(T min, T max, Visitor<T> v) {
		interval(min, max, v, this.root);
	}


    /**
     * Traverse the tree in inorder.
     * @param root Root of the tree.
	 * @param vis Visitor.
     */
	private void inorder(BinaryNode<T> node, Visitor<T> vis) {
		if (node != null) {
			inorder(node.getLeft(), vis);
			vis.visit(node.getToken());
			inorder(node.getRight(), vis);
		}
	}

	/**
	 * Traverse the tree in preorder.
	 * @param node Root of the tree.
	 * @param vis Visitor.
	 */
	private void preorder(BinaryNode<T> node, Visitor<T> vis) {
		if (node != null) {
			vis.visit(node.getToken());
			preorder(node.getLeft(), vis);
			preorder(node.getRight(), vis);
		}
	}

	/**
	 * Traverse the tree in postorder.
	 * @param node Root of the tree.
	 * @param vis Visitor.
	 */
	private void postorder(BinaryNode<T> node, Visitor<T> vis) {
		if (node != null) {
			postorder(node.getLeft(), vis);
			postorder(node.getRight(), vis);
			vis.visit(node.getToken());
		}
	}

	/**
	 * Traverse the tree in levelorder.
	 * @param node Root of the tree.
	 * @param visitor Visitor.
	 */
	private void levelorder(BinaryNode<T> node, Visitor<T> visitor) {
		Queue<BinaryNode<T>> q = new LinkedList<BinaryNode<T>>();
		if (node != null) {
			q.add(node);
		}
		while (!q.isEmpty()) {
			node = q.remove();
			visitor.visit(node.getToken());
			if (node.getLeft() != null) {
				q.add(node.getLeft());
			}
			if (node.getRight() != null) {
				q.add(node.getRight());
			}
		}
	}

	/**
	 * Traverse the tree in interval.
	 * @param min Minimum value.
	 * @param max Maximum value.
	 * @param v Visitor.
	 * @param node Root of the tree.
	 */
	private void interval(T min, T max, Visitor<T> v, BinaryNode<T> node) {
		if (node != null) {
			if (0 > node.getToken().compareTo(min)) {
				interval(min, max, v, node.getRight());
			} else if (0 < node.getToken().compareTo(max)) {
				interval(min, max, v, node.getLeft());
			} else {
				v.visit(node.getToken());
				interval(min, max, v, node.getLeft());
				interval(min, max, v, node.getRight());
			}
		}
	}
}