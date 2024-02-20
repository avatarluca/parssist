package parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import parssist.Config;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.Parser;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.exception.LRParseException;
import parssist.parser.util.Grammar;
import parssist.parser.util.Grammar.Item;
import parssist.parser.util.Stack;
import parssist.parser.util.tree.ParseTreeNode;
import parssist.parser.util.tree.visitor.JsonLikeTreeVisitor;


/**
 * A non recursive shift-reduce parser.
 */
abstract public class LRParser extends Parser {
    protected final Grammar grammar;

    private String w$;
    private Stack<ParseTreeNode> stack;
    private ParseTreeNode root;


    /**
     * Create a new LR parser.
     * @param grammar The grammar to parse.
     * @param w The input string.
     * @throws NullPointerException If the grammar or the input string is null.
     */
    public LRParser(final Grammar grammar, final String w) throws NullPointerException {
        this.grammar = grammar;
        grammar.addArgumentProduction(new Token(new TokenType(Config.GRAMMAR_ARGUMENT_SYMBOL, Config.GRAMMAR_ARGUMENT_SYMBOL, 0, false), Config.GRAMMAR_ARGUMENT_SYMBOL));
        
        setInputString(w);
        resetStack();
    }


    @Override public ParseTreeNode parse(final String w) throws Exception, LRParseException {
        final LRParseTable parseTable = createParseTable();

        setInputString(w);
        resetStack();

        int it = 0;
        int ip = 0;
        int state = parseTable.getStartStates().get(0);
        stack.push(new ParseTreeNode(state));

        while(it < Config.PARSELIMIT) {

            final ParseTreeNode top = stack.peek();
            if(top.hasState()) state = top.getState();

            final Token a = getNextToken(ip);

            if(a == null) throw new LRParseException(Config.BOTTOM_UP_PARSER_ERROR_INVALID_TOKEN + " " + ip + " " + w$);
            
            if(a.tokenType().ignore() && ip < w$.length() - 1) { // ignore token (e.g. whitespace, empty symbol except the last one ...) 
                ip += a.symbol().length();
                continue;
            }

            final int aIndex = parseTable.getAlphabet().indexOf(a);
            final Action action = parseTable.getActionTable()[state][aIndex];

            if(action.type == Action.Type.SHIFT) {
                stack.push(new ParseTreeNode(a));
                stack.push(new ParseTreeNode(action.value));

                ip += a.symbol().length();
            } else if(action.type == Action.Type.REDUCE) {
                final ParseTreeNode newNode = new ParseTreeNode(grammar.getProductions().get(action.value).getLhs());

                for(int i = 0; i < grammar.getProductions().get(action.value).getRhs().get(0).length * 2; i++) {
                    final ParseTreeNode node = stack.pop();
                    if(node.getToken() != null) updateTree(newNode, node);
                }
                
                int $state = state;
                if(stack.peek().hasState()) $state = stack.peek().getState();

                stack.push(newNode);
                stack.push(new ParseTreeNode(parseTable.getGotoTable()[$state][parseTable.getVocabulary().indexOf(grammar.getProductions().get(action.value).getLhs())]));
            } else if(action.type == Action.Type.ACCEPT) {
                if(stack.peek().getToken() == null) stack.pop();
                root = stack.peek();
                return stack.pop();
            }
            else if(action.type == Action.Type.ERROR) throw new LRParseException();

            it++;
        }

        throw new LRParseException(Config.BOTTOM_UP_PARSER_ERROR_PARSE_LIMIT);
    } 
    

    /**
     * Create a parse (action-goto-)table for the given grammar.
     * The action table is a 2D array, where the first index is the state and the second index is the terminal symbol.
     * The goto table is a 2D array, where the first index is the state and the second index is the non-terminal symbol.
     * @return The parse table.
     * @throw Exception if there was an error creating the parse table.
     */
    abstract public LRParseTable createParseTable() throws Exception;


    public ParseTreeNode getRoot() {
        return root;
    }

    
    /**
     * Set the input string.
     * @param w The input string.
     * @throws NullPointerException If the input string is null.
     */
    public void setInputString(final String w) throws NullPointerException {
        Objects.requireNonNull(w);

        this.w$ = w + Grammar.EMPTY_SYMBOL;
    }

    /**
     * Reset the stack.
     */
    public void resetStack() {
        this.stack = new Stack<>();
    }

    /**
     * Print the tree as JSON.
     * Because of webassembly, the tree can't be org.json.
     * So it uses a custom implementation of a JSON like object.
     * @throws NullPointerException If the root or its token are null.
     */
    public void printParseTree() throws NullPointerException {
        Objects.requireNonNull(root);
        Objects.requireNonNull(root.getToken());

        final JsonLikeTreeVisitor visitor = new JsonLikeTreeVisitor();
        root.accept(visitor);
        
        System.out.println(visitor.getJson());
    }
    

    /**
     * Get the next token from the input buffer.
     * Maybe sort by priority of the token types in the future.
     * @return The next token from the input buffer or null.
     */
    private @Nullable Token getNextToken(final int ip) {
        final String tempW$ = w$.substring(ip);

        for(final TokenType tokenType : grammar.getTokentypes()) { 
            if(tokenType.name().equals(Config.LEXER_NONTERMINAL)) continue;
            final Pattern pattern = Pattern.compile(Config.LEXER_REGEX_STARTSYMBOL + "(" +  tokenType.regex() + ")");
            final Matcher matcher = pattern.matcher(tempW$);

            if(matcher.find()) {
                final String value = matcher.group();
                return new Token(tokenType, value);
            }
        }

        return null;
    }

    /**
     * Update the tree.
     * @param root The root of the partition.
     * @param node The token to add.
     */
    private void updateTree(final ParseTreeNode root, final ParseTreeNode node) {
        root.addChild(node);
        node.setParent(root);
    }


    /**
     * A single action in the parse table.
     */
    public static class Action {
        public static enum Type {
            SHIFT, 
            REDUCE, 
            ACCEPT,
            ERROR
        }


        public final Type type;
        public final int value;


        /**
         * Create a new action.
         * @param type The type of the action.
         * @param value The value of the action.
         */
        public Action(Type type, int value) {
            this.type = type;
            this.value = value;
        }


        @Override public String toString() {
            return type + " " + value;
        }
    }

    /**
     * A parse table for the LR parser.
     * Represents mainly the action and goto table.
     */
    public static class LRParseTable {
        public final Grammar grammar;
        public final Action[][] actionTable;
        public final int[][] gotoTable;
        public final List<List<Item>> states;
        public final List<Token> alphabet;
        public final List<Token> vocabulary;
        public final List<Integer> startStates;


        /**
         * Create a new parse table.
         * @param actionTable The action table.
         * @param gotoTable The goto table.
         * @param states The states of the parser.
         * @param alphabet The alphabet of the grammar.
         * @param vocabulary The vocabulary of the grammar.
         */
        public LRParseTable(final Grammar grammar, final Action[][] actionTable, final int[][] gotoTable, final List<List<Item>> states, final List<Token> alphabet, final List<Token> vocabulary) {
            this.grammar = grammar;
            this.actionTable = actionTable;
            this.gotoTable = gotoTable;
            this.states = states;
            this.alphabet = alphabet;
            this.vocabulary = vocabulary;
            this.startStates = new ArrayList<>();

            for(List<Item> state : states) {
                for(Item item : state) {
                    if(item.getProduction().getLhs().symbol().equals(Config.GRAMMAR_ARGUMENT_SYMBOL) 
                    && item.getPosition() == 0
                    && grammar.isSymbolNonTerminal(item.getProduction().getRhs().get(0)[0].symbol())) {
                        startStates.add(states.indexOf(state));
                        break;
                    }
                }
            }
        }   


        @Override public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("Action Table:\n");
            for(int i = 0; i < actionTable.length; i++) {
                sb.append("State " + i + ":\n");
                for(int j = 0; j < actionTable[i].length; j++) {
                    sb.append("  " + alphabet.get(j).symbol() + ": " + actionTable[i][j] + "\n");
                }
            }

            sb.append("Goto Table:\n");
            for(int i = 0; i < gotoTable.length; i++) {
                sb.append("State " + i + ":\n");
                for(int j = 0; j < gotoTable[i].length; j++) {
                    sb.append("  " + vocabulary.get(j).symbol() + ": " + gotoTable[i][j] + "\n");
                }
            }

            return sb.toString();
        }


        public Action[][] getActionTable() {
            return actionTable;
        }

        public int[][] getGotoTable() {
            return gotoTable;
        }

        public List<List<Item>> getStates() {
            return states;
        }

        public List<Token> getAlphabet() {
            return alphabet;
        }

        public List<Token> getVocabulary() {
            return vocabulary;
        }

        public List<Integer> getStartStates() {
            return startStates;
        }


        /**
         * Prepare the short version of the parse table.
         * @return The short version of the parse table.
         */
        public String print() {
            String res = "";
            for(int i = 0; i < actionTable.length; i++) {
                for(int j = 0; j < actionTable[i].length; j++) {
                    res += actionTable[i][j] + ":";
                }
                res += ";";
            }
            for(int i = 0; i < gotoTable.length; i++) {
                for(int j = 0; j < gotoTable[i].length; j++) {
                    res += gotoTable[i][j] + ":";
                }
                res += ";";
            }

            return res;
        }
    }
}
