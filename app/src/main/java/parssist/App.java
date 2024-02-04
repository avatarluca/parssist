package parssist;

import java.io.IOException;
import java.util.*;

import parssist.lexer.Lexer;
import parssist.lexer.exception.InvalidLexFormatException;
import parssist.lexer.exception.InvalidTokenException;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.top_down_analysis.nrdparser.TabledrivenPredictiveParser;
import parssist.parser.top_down_analysis.nrdparser.exception.NoLL1GrammarException;
import parssist.parser.top_down_analysis.nrdparser.exception.NonRecursivePredictiveParseException;
import parssist.parser.top_down_analysis.nrdparser.util.Grammar;
import parssist.parser.top_down_analysis.nrdparser.util.Production;
import parssist.util.Reader;
import parssist.util.Writer;


public class App {
    private static final ParssistConfig CONFIG = ParssistConfig.getInstance();


    public static void main(String[] args) throws IOException, InvalidLexFormatException, InvalidTokenException, NonRecursivePredictiveParseException, NoLL1GrammarException {
        final Reader reader = new Reader();
        final Writer writer = new Writer();
        final Lexer lexer = new Lexer();

        final String code = reader.read(CONFIG.getProperty("PARSER.INIT.INPUT.DIR"));
        lexer.setCode(code);
        List<Token> tokens = lexer.tokenize();
        List<TokenType> tokenTypes = lexer.getTokenTypes();

        writer.write(CONFIG.getProperty("LEXER.TOKENTABLE.OUTPUT.DIR"), tokenTypes, tokens);
        TabledrivenPredictiveParser parser1 = new TabledrivenPredictiveParser(createGrammar(), "()");
        TabledrivenPredictiveParser parser2 = new TabledrivenPredictiveParser(createGrammar(), "(");
        System.out.println(parser1.computeSystemAnalysis());
        System.out.println(parser2.computeSystemAnalysis());

    }


    /**
     * Creates the following grammar:
     * S -> ( S ) S | $
     * @return The grammar.
     */
    private static Grammar createGrammar() {
        List<Production> productions = new ArrayList<>();
        List<Token> vocabulary = new ArrayList<>();
        List<Token> alphabet = new ArrayList<>();
        List<Token[]> list1 = new ArrayList<>();
        List<Token[]> list2 = new ArrayList<>();
        

        list1.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "("), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "S"),
            new Token(new TokenType("TERMINAL", "", 0, false), ")"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "S")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "S"
                ), 
                list1
            )
        );


        list2.add(new Token[] {
            new Token(new TokenType("EMPTY_SYMBOL", "", 0, false), "$")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "S"
                ), 
                list2
            )
        );


        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "S"
        ));
        
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "("
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), ")"
        ));
        alphabet.add(new Token(
            new TokenType("EMPTY_SYMBOL", "", 0, false), "$"
        ));

        Token startsymbol = new Token(
            new TokenType("NONTERMINAL", "", 0, false), "S"
        );
        
        List tokentypes = new ArrayList<>();
        tokentypes.add(new TokenType("TERMINAL", "\\(|\\)", 0, false));
        tokentypes.add(new TokenType("NONTERMINAL", "S", 0, false));
        tokentypes.add(new TokenType("EMPTY_SYMBOL", "$", 0, false));


        return new Grammar(tokentypes, vocabulary, alphabet, productions, startsymbol, true);
    }
}
