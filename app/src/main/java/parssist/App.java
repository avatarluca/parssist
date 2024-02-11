package parssist;

import java.io.IOException;

import parssist.lexer.Lexer;
import parssist.lexer.exception.InvalidLexFormatException;
import parssist.lexer.exception.InvalidTokenException;
import parssist.parser.top_down_analysis.nrdparser.generator.GrammarGenerator;
import parssist.parser.top_down_analysis.nrdparser.generator.TabledrivenPredictiveGenerator;
import parssist.parser.top_down_analysis.nrdparser.generator.exception.ParseException;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NoLL1GrammarException;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NonRecursivePredictiveParseException;


/**
 * The main class of the application.
 */
public class App {
    /**
     * The main method of the application. 
     * arg specs:
     * 1. lexInput: string
     * => the input string of the lex file
     * 2. grammarInput: string
     * => the input string of the grammar file
     * @param args The command line arguments.
     * @throws IOException If an I/O error occurs.
     * @throws InvalidLexFormatException If the lex file is invalid.
     * @throws InvalidTokenException If a token is invalid.
     * @throws NonRecursivePredictiveParseException If a non-recursive predictive parse exception occurs.
     * @throws NoLL1GrammarException If the grammar is not LL(1).
     * @throws ParseException If a parse exception occurs.
     */
    public static void main(String... args) throws IOException, InvalidLexFormatException, InvalidTokenException, NonRecursivePredictiveParseException, NoLL1GrammarException, ParseException {        
        String lex = "%\" \", \"\\t\", \"\\n" + //
                        "\", \"\\s\", \"\\r\"\r\n" + //
                        "TERMINAL := \"\\(\"\r\n" + //
                        "TERMINAL := \"\\)\"\r\n" + //
                        "EMPTY_SYMBOL := \"\\$\"\r\n" + //
                        "NONTERMINAL := \"S\"\r\n" + //
                        "NONTERMINAL := \"T\"", grammar = "";

        if(args.length > 0) lex = args[1];
        if(args.length > 1) grammar = args[2];

        final Lexer lexer = new Lexer(lex);
        lexer.parseTokens(lex);
        
        final GrammarGenerator generator = new GrammarGenerator(grammar, lex, lexer.getTokenTypes(), true);
        final TabledrivenPredictiveGenerator tabledrivenPredictiveGenerator = new TabledrivenPredictiveGenerator(generator.generate());
        tabledrivenPredictiveGenerator.generate("TestParser", "test.package");
        System.out.println(tabledrivenPredictiveGenerator.generate("TestParser", "test.package"));
    }
}
