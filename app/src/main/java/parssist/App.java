package parssist;

import java.io.IOException;
import java.util.*;

import parssist.lexer.Lexer;
import parssist.lexer.exception.InvalidLexFormatException;
import parssist.lexer.exception.InvalidTokenException;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.top_down_analysis.nrdparser.parser.TabledrivenPredictiveParser;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NoLL1GrammarException;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NonRecursivePredictiveParseException;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Grammar;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Production;
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
        
        TabledrivenPredictiveParser parser1 = new TabledrivenPredictiveParser(createGrammar(), "$");
        System.out.println(parser1.parse("$"));
    }


    /**
     * Creates the following grammar:
     * S -> TS | $
     * T -> (S)
     * @return The grammar.
     */
    private static Grammar createGrammar() {
        List<Production> productions = new ArrayList<>();
        List<Token> vocabulary = new ArrayList<>();
        List<Token> alphabet = new ArrayList<>();
        List<Token[]> list1 = new ArrayList<>();
        List<Token[]> list2 = new ArrayList<>();
        List<Token[]> list3 = new ArrayList<>();

        

        list1.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "T", 0, false), "T"),  
            new Token(new TokenType("NONTERMINAL", "S", 0, false), "S")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "S", 0, false), "S"
                ), 
                list1
            )
        );


        list2.add(new Token[] {
            new Token(new TokenType("EMPTY_SYMBOL", "\\$", 0, true), "$")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "S", 0, false), "S"
                ), 
                list2
            )
        );


        list3.add(new Token[] {
            new Token(new TokenType("TERMINAL", "(", 0, false), "("),  
            new Token(new TokenType("NONTERMINAL", "S", 0, false), "S"),
            new Token(new TokenType("TERMINAL", ")", 0, false), ")"),  
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "T", 0, false), "T"
                ), 
                list3
            )
        );


        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "S", 0, false), "S"
        ));
        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "T", 0, false), "T"
        ));
        
        alphabet.add(new Token(
            new TokenType("TERMINAL", "(", 0, false), "("
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", ")", 0, false), ")"
        ));
        alphabet.add(new Token(
            new TokenType("EMPTY_SYMBOL", "\\$", 0, true), "$"
        ));

        Token startsymbol = new Token(
            new TokenType("NONTERMINAL", "S", 0, false), "S"
        );
        
        List tokentypes = new ArrayList<>();
        tokentypes.add(new TokenType("TERMINAL", "\\(|\\)", 0, false));
        tokentypes.add(new TokenType("NONTERMINAL", "S", 0, false));
        tokentypes.add(new TokenType("EMPTY_SYMBOL", "\\$", 0, true));


        return new Grammar(tokentypes, vocabulary, alphabet, productions, startsymbol, true);
    }
}
