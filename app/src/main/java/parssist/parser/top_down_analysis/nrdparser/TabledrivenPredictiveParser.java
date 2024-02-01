package parssist.parser.top_down_analysis.nrdparser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.checkerframework.checker.units.qual.s;

import parssist.ParssistConfig;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.Parser;
import parssist.parser.top_down_analysis.nrdparser.exception.NonRecursivePredictiveParseException;
import parssist.parser.top_down_analysis.nrdparser.util.Grammar;
import parssist.parser.top_down_analysis.nrdparser.util.Production;
import parssist.parser.top_down_analysis.nrdparser.util.Stack;


/**
 * A non-recursive predictive parser implementation variant.
 * Consists of an input buffer, a stack, a parsetable and an output stream.
 * The input buffer contains the string that is to be analyzed. the string is terminated with the end marker {@link Grammar#EMPTY_SYMBOL}.
 */
public class TabledrivenPredictiveParser extends Parser {
    private static final ParssistConfig CONFIG = ParssistConfig.getInstance();

    private final Token EMPTY_TOKEN = new Token(new TokenType(CONFIG.getProperty("LEXER.EMPTY_SYMBOL"), Grammar.EMPTY_SYMBOL, 0, false), Grammar.EMPTY_SYMBOL);
    private final Grammar grammar;
    private final Stack<Token> stack;
    private final List<Production>[][] parseTable;

    private String w$;


    /**
     * Create a new non-recursive predictive parser.
     * Also creates a new stack and pushes a Token with {@link TabledrivenPredictiveParser#EMPTY_SYMBOL}.
     * @param grammar The grammar, which the parser is working.
     * @param w The input string.
     */
    public TabledrivenPredictiveParser(final Grammar grammar, final String w) {
        this.grammar = grammar;
        this.w$ = w + Grammar.EMPTY_SYMBOL;

        this.stack = new Stack<>();
        this.stack.push(EMPTY_TOKEN);
        this.stack.push(grammar.getStartsymbol());

        this.parseTable = createParseTable(grammar);
    }

    public TabledrivenPredictiveParser(final Grammar grammar) {
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

 
    /**
     * TODO
     * @return True if the input string is valid, false otherwise.
     * @throws NonRecursivePredictiveParseException If there is an exception while parsing the input.
     */
    public boolean computeSystemAnalysis() throws NonRecursivePredictiveParseException {
        final int ip = 0; // first symbol of w$
        /* 
        while(!stack.isEmpty()) {
            final Token X = stack.peek(); // top of stack
            final Token a = getNextToken(ip); // symbol of w$

            if(a == null) throw new NonRecursivePredictiveParseException(CONFIG.getProperty("NONREC.PARSER.ERROR.INVALID_TOKEN"));

            if(isTerminal(X) || X.symbol().equals(Grammar.EMPTY_SYMBOL)) {
                if(X.equals(a)) {
                    stack.pop();
                    ip += a.symbol().length();
                } else throw new NonRecursivePredictiveParseException(CONFIG.getProperty("NONREC.PARSER.ERROR.EMPTY_SYMBOL"));
            } else if(X.getTokenType().isTerminal()) {
                if(!token.getValue().equals(getNextToken().getValue())) return false;
            } else {
                final Token nextToken = getNextToken();
                final Token[] production = grammar.getProductions().stream()
                        .filter(p -> p.getLhs().getValue().equals(token.getValue()))
                        .filter(p -> p.getRhs()[0].getValue().equals(nextToken.getValue()))
                        .findFirst()
                        .orElse(null)
                        .getRhs();

                for(int i = production.length - 1; i >= 0; i--) {
                    stack.push(production[i]);
                }
            }
        }
*/
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
        if(grammar.isPreproc()) throw new IllegalArgumentException(CONFIG.getProperty("NONREC.PARSER.ERROR.PREPROCESSED"));

        final List<Production>[][] parseTable = new ArrayList[grammar.getVocabulary().size()][grammar.getAlphabet().size()];
      
        for(int i = 0; i < parseTable.length; i++) {
            for(int j = 0; j < parseTable[i].length; j++) {
                parseTable[i][j] = new ArrayList<>();
            }
        }

        for(final Token nonTerminal : grammar.getVocabulary()) {
            for(final Production production : grammar.getProductions()) {
                if(production.getLhs().equals(nonTerminal)) { // Gets all productions of a certain non terminal
                    Set<Token> first = grammar.first(nonTerminal.symbol());
                    Set<Token> follow = grammar.follow(nonTerminal.symbol());

                    for(final Token terminal : first) {
                        if(!terminal.equals(new Token(new TokenType(CONFIG.getProperty("LEXER.EMPTY_SYMBOL"), Grammar.EMPTY_SYMBOL, 0, false), Grammar.EMPTY_SYMBOL))) {
                            ArrayList<Production> productions = (ArrayList<Production>) parseTable[grammar.getVocabulary().indexOf(nonTerminal)][grammar.getAlphabet().indexOf(terminal)];
                            
                            if(checkProductionNotInList(productions, production)) {
                                productions.add(production);
                            }
                        }
                    }

                    if(hasSetEmptySymbol(first)) {
                        for(final Token terminal : follow) {
                            ArrayList<Production> productions = (ArrayList<Production>)parseTable[grammar.getVocabulary().indexOf(nonTerminal)][grammar.getAlphabet().indexOf(terminal)];
                            
                            if(checkProductionNotInList(productions, production)) {
                                productions.add(production);
                            }
                        }
                    
                    }
                }
            }
        }

        return parseTable;
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
     * @return The next token from the input buffer or null.
     */
    private @Nullable Token getNextToken(final int ip) {
        final String tempW$ = w$.substring(ip);
        for(final TokenType tokenType : grammar.getTokentypes()) { // TODO: sort by priority
            final Pattern pattern = Pattern.compile(CONFIG.getProperty("LEXER.REGEX.STARTSYMBOL") + tokenType.regex());
            final Matcher matcher = pattern.matcher(tempW$);

            if(matcher.find()) {
                final String value = matcher.group();
                return new Token(tokenType, value);
            }
        }

        return null;
    }

    /**
     * Check if a token is a terminal.
     * @param token The token to check.
     * @return True if the token is a terminal, false otherwise.
     */
    private boolean isTerminal(final Token token) {
        return token.tokenType().name().equals(CONFIG.getProperty("LEXER.TERMINAL"));
    }
}