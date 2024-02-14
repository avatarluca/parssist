package parssist.parser.top_down_analysis.nrdparser.parser.util;

import java.util.ArrayList;
import java.util.List;


/**
 * Stack class, used to represent a stack.
 */
public class Stack<T> {
    private List<T> stack;


    /**
     * Create a new stack.
     */
    public Stack() {
        this.stack = new ArrayList<>();
    }


    /**
     * Push a token to the stack.
     * @param token The token to push.
     */
    public void push(final T token) {
        stack.add(token);
    }

    /**
     * Pop a token from the stack.
     * @return The popped token.
     */
    public T pop() {
        return stack.remove(stack.size() - 1);
    }

    /**
     * Peek the top token from the stack.
     * @return The peeked token.
     */
    public T peek() {
        return stack.get(stack.size() - 1);
    }

    /**
     * Check if the stack is empty.
     * @return True if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Get the size of the stack.
     * @return The size of the stack.
     */
    public int size() {
        return stack.size();
    }


    @Override public String toString() {
        return stack.stream()
                    .map(Object::toString)
                    .reduce((a, b) -> a + "\n+ " + b)
                    .orElse("");
    }
}