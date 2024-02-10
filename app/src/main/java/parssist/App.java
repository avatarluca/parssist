package parssist;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.checkerframework.checker.units.qual.s;

import parssist.lexer.Lexer;
import parssist.lexer.exception.InvalidLexFormatException;
import parssist.lexer.exception.InvalidTokenException;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.top_down_analysis.nrdparser.generator.GrammarGenerator;
import parssist.parser.top_down_analysis.nrdparser.generator.TabledrivenPredictiveGenerator;
import parssist.parser.top_down_analysis.nrdparser.generator.exception.ParseException;
import parssist.parser.top_down_analysis.nrdparser.parser.TabledrivenPredictiveParser;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NoLL1GrammarException;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NonRecursivePredictiveParseException;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Grammar;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Production;
import parssist.util.Reader;
import parssist.util.Writer;


public class App {
    private static final ParssistConfig CONFIG = ParssistConfig.getInstance();


    public static void main(String[] args) throws IOException, InvalidLexFormatException, InvalidTokenException, NonRecursivePredictiveParseException, NoLL1GrammarException, ParseException {
        final Reader reader = new Reader();
        final Writer writer = new Writer();
        final Lexer lexer = new Lexer();

        /* 
        final String code = reader.read(CONFIG.getProperty("PARSER.INIT.INPUT.DIR"));
        lexer.setCode(code);
        List<Token> tokens = lexer.tokenize();
        List<TokenType> tokenTypes = lexer.getTokenTypes();

        writer.write(CONFIG.getProperty("LEXER.TOKENTABLE.OUTPUT.DIR"), tokenTypes, tokens);

        TabledrivenPredictiveParser parser1 = new TabledrivenPredictiveParser(createGrammar(), "$");
        System.out.println(parser1.parse("$"));

        TabledrivenPredictiveGenerator generator = new TabledrivenPredictiveGenerator(createGrammar());
        System.out.println(generator.generate("TestParser", "test.package"));
        */

        final String lex = reader.read(CONFIG.getProperty("LEXER.INIT.INPUT.DIR"));
        final String grammar = reader.read(CONFIG.getProperty("PARSER.INIT.INPUT.DIR"));        
        final Lexer lGrammarGen = new Lexer();
        lGrammarGen.parseTokens(lex);
        GrammarGenerator generator = new GrammarGenerator(grammar, lGrammarGen.getTokenTypes(), true);
        // System.out.println(generator.generate());   
        // g.getAlphabet().forEach(e -> System.out.println(e.symbol()));
        // TabledrivenPredictiveGenerator gen = new TabledrivenPredictiveGenerator(generator.generate());
        // System.out.println(gen.generate("TestParser", "test.package"));
        
        Grammar grammar1 = generator.generate();
        Grammar grammar2 = createGrammar();

      
        /**
         * System.out.println("Alphabet");
        System.out.println("first");
        grammar1.getAlphabet().forEach(e -> System.out.println(e));
        System.out.println("second");
        grammar2.getAlphabet().forEach(e -> System.out.println(e));
        System.out.println("Vocabulary");
        System.out.println("first");
        grammar1.getVocabulary().forEach(e -> System.out.println(e));
        System.out.println("second");
        grammar2.getVocabulary().forEach(e -> System.out.println(e));
        System.out.println("Productions");
        System.out.println("first");
        grammar1.getProductions().forEach(e -> System.out.println(e));
        System.out.println("second");
        grammar2.getProductions().forEach(e -> System.out.println(e));
        System.out.println("TokenTypes");
        System.out.println("first");
        grammar1.getTokentypes().forEach(e -> System.out.println(e));
        System.out.println("second");
        grammar2.getTokentypes().forEach(e -> System.out.println(e));
        
        System.out.println("Productions details");
        System.out.println("first");
        for(Production production : grammar1.getProductions()) {
            System.out.println(">" + production);
            for(Token[] rhs : production.getRhs()) {
                for(Token token : rhs) {
                    System.out.println(token);
                }
            }
        }
        System.out.println("second");
        for(Production production : grammar2.getProductions()) {
            System.out.println(">" + production);
            for(Token[] rhs : production.getRhs()) {
                for(Token token : rhs) {
                    System.out.println(token);
                }
            }
        }
         */


        TabledrivenPredictiveGenerator gen = new TabledrivenPredictiveGenerator(grammar1);
        gen.generate("TestParser", "test.package");
        System.out.println(gen.generate("TestParser", "test.package"));
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
            new Token(new TokenType("NONTERMINAL", "T", 4, false), "T"),  
            new Token(new TokenType("NONTERMINAL", "S", 3, false), "S")
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
            new Token(new TokenType("EMPTY_SYMBOL", "\\$", 2, false), "$")
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
            new Token(new TokenType("TERMINAL", "\\(", 0, false), "("),  
            new Token(new TokenType("NONTERMINAL", "S", 3, false), "S"),
            new Token(new TokenType("TERMINAL", "\\)", 1, false), ")"),  
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
            new TokenType("NONTERMINAL", "T", 1, false), "T"
        ));
        
        alphabet.add(new Token(
            new TokenType("TERMINAL", "\\(", 0, false), "("
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "\\)", 1, false), ")"
        ));
        alphabet.add(new Token(
            new TokenType("EMPTY_SYMBOL", "\\$", 2, false), "$"
        ));

        Token startsymbol = new Token(
            new TokenType("NONTERMINAL", "S", 0, false), "S"
        );
        
        List tokentypes = new ArrayList<>();
        tokentypes.add(new TokenType("TERMINAL", "\\(", 0, false));
        tokentypes.add(new TokenType("TERMINAL", "\\)", 1, false));
        tokentypes.add(new TokenType("NONTERMINAL", "S", 0, false));
        tokentypes.add(new TokenType("NONTERMINAL", "T", 1, false));
        tokentypes.add(new TokenType("EMPTY_SYMBOL", "\\$", 2, true));


        return new Grammar(tokentypes, vocabulary, alphabet, productions, startsymbol, true);
    }
}
