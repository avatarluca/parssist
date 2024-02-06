package parssist.parser.top_down_analysis.nrdparser.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import parssist.ParssistConfig;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.Parser;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NoLL1GrammarException;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NonRecursivePredictiveParseException;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Grammar;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Production;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Stack;
import parssist.parser.top_down_analysis.nrdparser.parser.util.tree.ParseTreeNode;
import parssist.parser.top_down_analysis.nrdparser.parser.util.tree.visitor.JsonTreeVisitor;


/**
 * A non-recursive predictive parser implementation variant.
 * Consists of an input buffer, a stack, a parsetable and an output stream.
 * The input buffer contains the string that is to be analyzed. the string is terminated with the end marker {@link Grammar#EMPTY_SYMBOL}.
 */
public class TabledrivenPredictiveParser extends Parser {
    private static final ParssistConfig CONFIG = ParssistConfig.getInstance();

    private final Token EMPTY_TOKEN = new Token(new TokenType(CONFIG.getProperty("LEXER.EMPTY_SYMBOL"), Grammar.EMPTY_SYMBOL, 0, false), Grammar.EMPTY_SYMBOL);
    private final Grammar grammar;
    private final List<Production>[][] parseTable;

    private Stack<ParseTreeNode> stack;
    private String w$;
    private ParseTreeNode root;


    /**
     * Create a new non-recursive predictive parser.
     * Also creates a new stack and pushes a Token with {@link TabledrivenPredictiveParser#EMPTY_SYMBOL}.
     * @param grammar The grammar, which the parser is working.
     * @param w The input string.
     * @throws IllegalArgumentException If the grammar is not preprocessed.
     */
    public TabledrivenPredictiveParser(final Grammar grammar, final String w) throws IllegalArgumentException {
        this.grammar = grammar;

        this.parseTable = createParseTable(grammar);
        this.root = new ParseTreeNode(grammar.getStartsymbol());

        setInputString(w);
        resetStack();
    }


    @Override public ParseTreeNode parse(final String w) throws NonRecursivePredictiveParseException, NoLL1GrammarException {
        setInputString(w);
        resetStack();
        computeSystemAnalysis();

        return root;
    }


    /**
     * Create a new non-recursive predictive parser with an empty input string.
     * @param grammar The grammar, which the parser is working.
     * @throws IllegalArgumentException If the grammar is not preprocessed.
     */
    public TabledrivenPredictiveParser(final Grammar grammar) throws IllegalArgumentException {
        this(grammar, "");
    }  


    /**
     * Get the input string.
     * @return The input string.
     */ 
    public String getInputString() {
        return w$.substring(0, w$.length() - 1);
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

    public ParseTreeNode getRoot() {
        return root;
    }

    public List<Production>[][] getParseTable() {
        return parseTable;
    }


    /**
     * Reset the stack.
     */
    public void resetStack() {
        this.stack = new Stack<>();
        this.stack.push(new ParseTreeNode(EMPTY_TOKEN));
        this.stack.push(root);
    }

    /**
     * Print parse table.
     */
    public void printParseTable() {
        for(int i = 0; i < parseTable.length; i++) {
            for(int j = 0; j < parseTable[i].length; j++) {
                System.out.print("[" + i + "][" + j + "]: ");
                for(Production production : parseTable[i][j]) {
                    System.out.print(production + " ");
                }
                System.out.println();
            }
        }
    }

    /**
     * Print the tree as JSON.
     * @throws NullPointerException If the root or its token are null.
     */
    public void printParseTree() throws NullPointerException {
        Objects.requireNonNull(root);
        Objects.requireNonNull(root.getToken());

        final JsonTreeVisitor visitor = new JsonTreeVisitor();
        root.accept(visitor);

        System.out.println(visitor.getJson());
    }
 
    /**
     * Compute the system analysis: Checks if a word is valid according to the grammar and in the language of the grammar.
     * @return True if the input string is valid, false otherwise.
     * @throws NonRecursivePredictiveParseException If there is an exception while parsing the input.
     * @throws NoLL1GrammarException If the grammar is not LL(1).
     */
    public boolean computeSystemAnalysis() throws NonRecursivePredictiveParseException, NoLL1GrammarException {
        if(!isLL1(parseTable)) throw new NoLL1GrammarException(CONFIG.getProperty("NONREC.PARSER.ERROR.NO_LL1_GRAMMAR"));

        root.cleanChildren();

        int ip = 0; // first symbol of w$
        ParseTreeNode X = stack.peek(); // top of stack
        ParseTreeNode a = new ParseTreeNode(getNextToken(ip)); // symbol of w$
        ParseTreeNode cursor = root;

        do {
            a = new ParseTreeNode(getNextToken(ip));
            cursor = X;
            
            if(a.getToken() == null) throw new NonRecursivePredictiveParseException(CONFIG.getProperty("NONREC.PARSER.ERROR.INVALID_TOKEN") + " " + ip + " " + w$);

            if(a.getToken().tokenType().ignore() && ip < w$.length() - 1) { // ignore token (e.g. whitespace, empty symbol except the last one ...) 
                ip += a.getToken().symbol().length();
                continue;
            }

            if(grammar.isSymbolTerminal(X.getToken().symbol()) || X.getToken().symbol().equals(Grammar.EMPTY_SYMBOL)) {
                if(X.equals(a)) {
                    stack.pop();

                    updateTree(cursor.getParent(), a);

                    ip += a.getToken().symbol().length();
                } else throw new NonRecursivePredictiveParseException(CONFIG.getProperty("NONREC.PARSER.ERROR.EMPTY_SYMBOL") + stack + " " + a.getToken().symbol() + " " + ip);
            } else { 
                try {
                    final Production production = parseTable[grammar.getVocabulary().indexOf(X.getToken())][grammar.getAlphabet().indexOf(a.getToken())].get(0);

                    if(production == null) throw new NonRecursivePredictiveParseException(CONFIG.getProperty("NONREC.PARSER.ERROR.NO_PRODUCTION"));

                    stack.pop();
                    
                    final Token[] rhs = production.getRhs().get(0); // first production because grammar preprocessing contract
                    for(int i = rhs.length - 1; i >= 0; i--) {
                        final Token tempToken = rhs[i];
                        final ParseTreeNode tempNode = new ParseTreeNode(tempToken);

                        updateTree(cursor, tempNode);

                        stack.push(tempNode); 
                    }
                } catch (IndexOutOfBoundsException e) {
                    throw new NonRecursivePredictiveParseException(CONFIG.getProperty("NONREC.PARSER.ERROR.NO_PRODUCTION"));
                }
            }

            while(stack.peek().getToken().equals(EMPTY_TOKEN) && stack.size() > 1) updateTree(stack.peek().getParent(), stack.pop());
        } while(!((X = stack.peek()).getToken().equals(EMPTY_TOKEN) && a.getToken().equals(EMPTY_TOKEN))); // stack and input buffer is empty
        
        return true;
    }


    /**
     * Creates a LL(1) parse table.
     * The y-axis is the vocabulary sorted by {@link Grammar#getVocabulary()}, the x-axis is the alphabet sorted by {@link Grammar#getAlphabet()}.
     * The grammar should be preprocessed (see {@link Grammar#splitProductions(List)}).
     * @param grammar The grammar.
     * @return The parse table.
     * @throws IllegalArgumentException If the grammar is not preprocessed.
     */
    @SuppressWarnings("unchecked") 
    List<Production>[][] createParseTable(final Grammar grammar) throws IllegalArgumentException {
        if(!grammar.isPreproc()) throw new IllegalArgumentException(CONFIG.getProperty("NONREC.PARSER.ERROR.PREPROCESSED"));

        final List<Production>[][] parseTable = new ArrayList[grammar.getVocabulary().size()][grammar.getAlphabet().size()];
      
        for(int i = 0; i < parseTable.length; i++) {
            for(int j = 0; j < parseTable[i].length; j++) {
                parseTable[i][j] = new ArrayList<>();
            }
        }

        for(final Token nonTerminal : grammar.getVocabulary()) {
            Set<Token> first = grammar.first(nonTerminal.symbol());
            Set<Token> follow = grammar.follow(nonTerminal.symbol(), nonTerminal.symbol());

            for(final Production production : grammar.getProductions()) {
                if(production.getLhs().equals(nonTerminal)) { // Gets all productions of a certain non terminal
                    for(final Token terminal : first) {
                        if(!production.hasEmptySymbol() && !terminal.equals(new Token(new TokenType(CONFIG.getProperty("LEXER.EMPTY_SYMBOL"), Grammar.EMPTY_SYMBOL, 0, false), Grammar.EMPTY_SYMBOL))) {
                            ArrayList<Production> productions = (ArrayList<Production>) parseTable[grammar.getVocabulary().indexOf(nonTerminal)][grammar.getAlphabet().indexOf(terminal)];
                            
                            if(checkProductionNotInList(productions, production) && (production.getRhs().get(0)[0].tokenType().name().equals(CONFIG.getProperty("LEXER.TERMINAL")) ? terminal.equals(production.getRhs().get(0)[0]) : true)){
                                productions.add(production);
                            }
                        } 
                    }

                    if(hasSetEmptySymbol(first)) {
                        for(final Token terminal : follow) {
                            ArrayList<Production> productions = (ArrayList<Production>)parseTable[grammar.getVocabulary().indexOf(nonTerminal)][grammar.getAlphabet().indexOf(terminal)];

                            if(checkProductionNotInList(productions, production)) {
                                Token[] tokens = new Token[]{new Token(new TokenType(CONFIG.getProperty("LEXER.EMPTY_SYMBOL"), Grammar.EMPTY_SYMBOL, 0, false),  Grammar.EMPTY_SYMBOL)};
                                List<Token[]> rhs = new ArrayList<>();
                                rhs.add(tokens);
                                productions.add(new Production(production.getLhs(), rhs));
                            }
                        }
                    }
                }
            }
        }

        return parseTable;
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
     * Checks if a production is already in a list.
     * @param productions The list of productions.
     * @param production The production to check.
     * @return True if the production is not in the list, false otherwise.
     */
    private boolean checkProductionNotInList(final ArrayList<Production> productions, final Production production) {
        for(final Production p : productions) {
            if(p.equals(production)) return false;
        }

        return true;
    }

    /**
     * Checks if a set contains the empty symbol.
     * @param set The set to check.
     * @return True if the set contains the empty symbol, false otherwise.
     */
    private boolean hasSetEmptySymbol(final Set<Token> set) {
        for(final Token token : set) {
            if(token.equals(new Token(new TokenType(CONFIG.getProperty("LEXER.EMPTY_SYMBOL"), Grammar.EMPTY_SYMBOL, 0, false), Grammar.EMPTY_SYMBOL))) return true;
        }

        return false;
    }

    /**
     * Get the next token from the input buffer.
     * Maybe sort by priority of the token types in the future.
     * @return The next token from the input buffer or null.
     */
    private @Nullable Token getNextToken(final int ip) {
        final String tempW$ = w$.substring(ip);

        for(final TokenType tokenType : grammar.getTokentypes()) { 
            if(tokenType.name().equals(CONFIG.getProperty("LEXER.NONTERMINAL"))) continue;
            final Pattern pattern = Pattern.compile(CONFIG.getProperty("LEXER.REGEX.STARTSYMBOL") + "(" +  tokenType.regex() + ")");
            final Matcher matcher = pattern.matcher(tempW$);

            if(matcher.find()) {
                final String value = matcher.group();
                return new Token(tokenType, value);
            }
        }

        return null;
    }

    /**
     * Is grammar LL(1)?
     * LL(1) grammars are a subset of context-free grammars:
     * - L: Left-to-right scan of the input
     * - L: Leftmost derivation
     * - 1: One token of lookahead
     * A left recursive or ambiguous grammar can never be LL(1).
     * @param parseTable The parse table.
     * @return True if the grammar is LL(1), false otherwise.
     */
    private boolean isLL1(final List<Production>[][] parseTable) {
        for(int i = 0; i < parseTable.length; i++) {
            for(int j = 0; j < parseTable[i].length; j++) {
                if(parseTable[i][j].size() > 1) return false;
            }
        }

        return true;
    }
}