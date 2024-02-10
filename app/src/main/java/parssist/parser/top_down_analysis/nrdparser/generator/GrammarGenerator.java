package parssist.parser.top_down_analysis.nrdparser.generator;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import parssist.ParssistConfig;
import parssist.lexer.Lexer;
import parssist.lexer.exception.InvalidLexFormatException;
import parssist.lexer.exception.InvalidTokenException;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.top_down_analysis.nrdparser.generator.exception.ParseException;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Grammar;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Production;
import parssist.util.Reader;


/**
 * Class to generate the {@link parssist.parser.top_down_analysis.nrdparser.parser.util.Grammar grammar}.
 */
public class GrammarGenerator {
    private static final ParssistConfig CONFIG = ParssistConfig.getInstance();

    private String lex;
    private String grammar;
    private List<TokenType> userTokenTypes;
    private boolean preproc;

    private List<Token> vocabulary;
    private List<Token> alphabet;
    private List<Production> productions;
    private Token startsymbol;
     

    /**
     * Create a new grammar generator.
     * @param grammar The grammar.
     * @param userTokenTypes The user defined token types.
     * @param preproc If the grammar should be preprocessed.
     * @throws IOException If the grammar lex couldn't be read.
     */
    public GrammarGenerator(final String grammar, final List<TokenType> userTokenTypes, final boolean preproc) throws IOException {
        this.grammar = grammar;
        this.userTokenTypes = userTokenTypes;
        this.preproc = preproc;

        final Reader reader = new Reader();
        this.lex = reader.read(CONFIG.getProperty("NONREC.PARSER.GRAMMARGENERATOR.INIT.INPUT.DIR"));
    }

    /**
     * Create a new grammar generator.
     * Preprocessing is enabled as default.
     * @param grammar The grammar.
     * @param userTokenTypes The user defined token types.
     * @throws IOException If the grammar lex couldn't be read.
     */
    public GrammarGenerator(final String grammar, final List<TokenType> userTokenTypes) throws IOException {
        this(grammar, userTokenTypes, true);
    }
  

    public String getLex() {
        return lex;
    }

    public String getGrammar() {
        return grammar;
    }

    public List<TokenType> getUserTokenTypes() {
        return userTokenTypes;
    }

    public boolean isPreproc() {
        return preproc;
    }

    public void setLex(final String lex) {
        this.lex = lex;
    }

    public void setGrammar(final String grammar) {
        this.grammar = grammar;
    }

    public void setUserTokenTypes(final List<TokenType> userTokenTypes) {
        this.userTokenTypes = userTokenTypes;
    }

    public void setPreproc(final boolean preproc) {
        this.preproc = preproc;
    }
    

    /**
     * Generate the grammar.
     * @return The grammar.
     * @throws InvalidLexFormatException If the lex is invalid.
     * @throws IOException If the lex couldn't be read.
     * @throws InvalidTokenException If the token is invalid.
     * @throws ParseException If the grammar couldn't be parsed.
     */
    public Grammar generate() throws IOException, InvalidLexFormatException, InvalidTokenException, ParseException {
        final Lexer lexer = new Lexer();

        lexer.setCode(grammar);
        lexer.parseTokens(lex);

        final List<Token> tokens = lexer.tokenize();
        final List<TokenType> tokenTypes = lexer.getTokenTypes();

        parseGrammar(tokens, tokenTypes);
        renewTokenTypes(tokenTypes);
        
        return new Grammar(tokenTypes, vocabulary, alphabet, productions, startsymbol, preproc);
    }


    /**
     * Handcoded parser. In the future this could also be done by inventing a grammar and put it in the created parsergenerator.
     * Parse the grammar and puts the result into the fields.
     * @param tokens The tokens.
     * @param tokenTypes The token types.
     * @throws ParseException If the grammar couldn't be parsed.
     */
    private void parseGrammar(final List<Token> tokens, final List<TokenType> tokenTypes) throws ParseException {
        Token currentToken = null;
        Token nonTerminalToken = null;
        int priority = 0;

        vocabulary = new ArrayList<>();
        alphabet = new ArrayList<>();
        productions = new ArrayList<>();

        while(!grammar.isEmpty() && (currentToken = eat(tokenTypes)) != null) {
            if(currentToken.tokenType().ignore()) continue;

            if(currentToken.tokenType().name().equals(CONFIG.getProperty("GRAMMAR.TOKEN.NONTERMINAL"))) nonTerminalToken = currentToken;  
            else if(currentToken.tokenType().name().equals(CONFIG.getProperty("GRAMMAR.TOKEN.PRODUCTION_RULE")) && nonTerminalToken != null) {
                do {
                    parseProduction(nonTerminalToken, currentToken, priority);
                } while((currentToken = eat(tokenTypes)) != null 
                    && !grammar.isEmpty() 
                    && currentToken.tokenType().name().equals(CONFIG.getProperty("GRAMMAR.TOKEN.PRODUCTION_RULE"))
                );

                priority++;
            }
        }

        if(!productions.isEmpty()) {
            startsymbol = productions.get(0).getLhs();
            
            for(final Production production : productions) {
                final Token lhs = production.getLhs();
                vocabulary.add(lhs.withTokenType(lhs.tokenType().withRegex(lhs.symbol())));

                List<Token> extractedAlphabet = userTokenTypes.stream()
                    .filter(e -> !e.name().equals(CONFIG.getProperty("GRAMMAR.TOKEN.NONTERMINAL")))
                    .map(e -> new Token(e, e.regex()))
                    .toList();
                
                for(final Token token : extractedAlphabet) {
                    final Token decoratedToken = token.withSymbol(token.symbol().replace("\\", ""));
                    if(!alphabet.contains(decoratedToken) && !decoratedToken.tokenType().ignore()) alphabet.add(decoratedToken);
                }
            }
        }
    }

    /**
     * Parse a production.
     * @param nonterminal The nonterminal of the lhs.
     * @param rule The rule of the rhs.
     * @param priority The priority of the production.
     * @throws ParseException
     */
    private void parseProduction(final Token nonterminal, final Token rule, final int priority) throws ParseException {
        if(!rule.tokenType().name().equals(CONFIG.getProperty("GRAMMAR.TOKEN.PRODUCTION_RULE"))) throw new ParseException(CONFIG.getProperty("GRAMMAR.TOKEN.ERROR.PRODUCTION_RULE"));
        
        final List<Token> catchedTokens = new ArrayList<>();
        final String ruleString = rule.symbol().replaceAll(CONFIG.getProperty("GRAMMAR.TOKEN.PRODUCTIONSYMBOLS.REGEX"), "");
        
        int ip = 0;

        while(ip < ruleString.length()) {
            final Token token = getNextToken(ip, ruleString, userTokenTypes);

            if(token == null) throw new ParseException(CONFIG.getProperty("GRAMMAR.TOKEN.ERROR.PRODUCTION_RULE"));

            ip += token.symbol().length();

            if(token.tokenType().ignore()) continue;

            catchedTokens.add(token);
        }

        final Token[] rhsTokens = catchedTokens.toArray(new Token[catchedTokens.size()]);
        final List<Token[]> rhs = new ArrayList<>();
        rhs.add(rhsTokens);

        if(productions.stream().map(e -> e.getLhs()).anyMatch(e -> e.equals(nonterminal))) {
            final Production production = productions.stream().filter(e -> e.getLhs().equals(nonterminal)).findFirst().get();
            production.getRhs().addAll(rhs);
        } else {
            productions.add(new Production(nonterminal.withTokenType(nonterminal.tokenType().withPriority(priority)), rhs));
        }
    }

    /**
     * Matches {@link grammar}.
     * @param tokenTypes The token types to match.
     * @return The next token from {@link grammar} or null if there is no next or unknown token.
     */
    private @Nullable Token eat(final List<TokenType> tokenTypes) {
        for(final TokenType tokenType : tokenTypes) {
            final Pattern pattern = Pattern.compile(CONFIG.getProperty("LEXER.REGEX.STARTSYMBOL") + tokenType.regex());
            final Matcher matcher = pattern.matcher(this.grammar);

            if(matcher.find()) {
                final String value = matcher.group();
                this.grammar = this.grammar.substring(value.length());
                return new Token(tokenType, value);
            }
        }

        return null;
    }

    /**
     * Get the next token from a input string.
     * Maybe sort by priority of the token types in the future.
     * It differs from the {@link GrammarGenerator#eat(List)} function, because it doesn't consume the input string.
     * @param ip The input position.
     * @param w$ The input string.
     * @param tokenTypes The token types to match.
     * @return The next token from the input buffer or null.
     */
    private @Nullable Token getNextToken(final int ip, final String w$, final List<TokenType> tokenTypes) {
        final String tempW$ = w$.substring(ip);

        for(final TokenType tokenType : tokenTypes) { 
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
     * Renews the token types:
     * - Removes the production rule and nonterminal token type.
     * - Adds the alphabet token types.
     * - Adds the vocabulary token types.
     * - Sets empty symbol to ignore.
     * - removes all ignoreables.
     * @param tokenTypes The token types to renew.
     */
    private void renewTokenTypes(final List<TokenType> tokenTypes) {
        tokenTypes.removeIf(e -> e.name().equals(CONFIG.getProperty("NONREC.PARSER.GRAMMARGENERATOR.PRODUCTION_RULE")));
        tokenTypes.removeIf(e -> e.name().equals(CONFIG.getProperty("NONREC.PARSER.GRAMMARGENERATOR.NONTERMINAL")));
        tokenTypes.addAll(alphabet.stream().map(e -> e.tokenType()).toList());
        tokenTypes.addAll(vocabulary.stream().map(e -> e.tokenType()).toList());

        final Iterator<TokenType> iter = tokenTypes.iterator();
        TokenType emptySymbol = null;
        while(iter.hasNext()) {
            final TokenType tokenType = iter.next();
            if(tokenType.name().equals(CONFIG.getProperty("NONREC.PARSER.GRAMMARGENERATOR.EMPTY_SYMBOL"))) {
                emptySymbol = tokenType;
                iter.remove();
            } 
            else if(tokenType.ignore()) iter.remove();
        }

        if(emptySymbol != null) tokenTypes.add(emptySymbol.withIgnore(true));
    }
}
