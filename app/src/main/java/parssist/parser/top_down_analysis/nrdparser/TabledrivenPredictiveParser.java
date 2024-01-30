package parssist.parser.top_down_analysis.nrdparser;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import parssist.ParssistConfig;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.Parser;
import parssist.parser.top_down_analysis.nrdparser.exception.NonRecursivePredictiveParseException;
import parssist.parser.top_down_analysis.nrdparser.util.Grammar;
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