package parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import parssist.Config;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.LRParser.LRParseTable;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.exception.NoSLR1GrammarException;
import parssist.parser.util.Grammar;
import parssist.parser.util.Production;


/**
 * Testclass for {@link SLRParser}.
 */
public class SLRParserTest {
    private List<Production> productions;
    private List<Token> vocabulary; 
    private List<Token> alphabet;
    private Token startsymbol;
    private Grammar grammar;
    private List<Token[]> list2;
    private List<Token[]> list3;
    private List<Token[]> list4;


    /**
     * Set up the grammar, by using the following grammar:
     * E_ -> E
     * E -> E + T | T
     * T -> T*F | F
     * F -> (E) | id
     * (Example 4.19 from the {@link <a href="https://suif.stanford.edu/dragonbook/">Dragonbook</a>})
     */
    public void setUp1() {
        productions = new ArrayList<>();
        vocabulary = new ArrayList<>();
        alphabet = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();


        list2.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E"),
            new Token(new TokenType("TERMINAL", "", 0, false), "+"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T")
        });
        list2.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "E"
                ), 
                list2
            )
        );


        list3.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T"),
            new Token(new TokenType("TERMINAL", "", 0, false), "*"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "F")
        });
        list3.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "F")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "T"
                ), 
                list3
            )
        );


        list4.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "("), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E"),
            new Token(new TokenType("TERMINAL", "", 0, false), ")")
        });
        list4.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "id")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "F"
                ), 
                list4
            )
        );


        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "E"
        ));
        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "T"
        ));
        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "F"
        ));
        
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "("
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), ")"
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "*"
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "+"
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "id"
        ));
        alphabet.add(new Token(
            new TokenType("EMPTY_SYMBOL", "", 0, false), "$"
        ));

        startsymbol = new Token(
            new TokenType("NONTERMINAL", "", 0, false), "E"
        );
        

        grammar = new Grammar(new ArrayList<>(), vocabulary, alphabet, productions, startsymbol, true);
        grammar.addArgumentProduction(new Token(new TokenType("NONTERMINAL", Config.GRAMMAR_ARGUMENT_SYMBOL, 0, false), Config.GRAMMAR_ARGUMENT_SYMBOL));
    }

    /**
     * Set up the grammar, by using the following grammar:
     * S -> L=R | R
     * L -> *R | id
     * R -> L
     * (Example 4.20 from the {@link <a href="https://suif.stanford.edu/dragonbook/">Dragonbook</a>})
     */
    public void setUp2() {
        productions = new ArrayList<>();
        vocabulary = new ArrayList<>();
        alphabet = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();


        list2.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "L"),
            new Token(new TokenType("TERMINAL", "", 0, false), "="), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "R")
        });
        list2.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "R")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "S"
                ), 
                list2
            )
        );


        list3.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "*"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "R")
        });
        list3.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "id")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "L"
                ), 
                list3
            )
        );


        list4.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "L")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "R"
                ), 
                list4
            )
        );


        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "S"
        ));
        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "L"
        ));
        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "R"
        ));
        
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "id"
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "="
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "*"
        ));
        alphabet.add(new Token(
            new TokenType("EMPTY_SYMBOL", "", 0, false), "$"
        ));

        startsymbol = new Token(
            new TokenType("NONTERMINAL", "", 0, false), "S"
        );
        

        grammar = new Grammar(new ArrayList<>(), vocabulary, alphabet, productions, startsymbol, true);
        grammar.addArgumentProduction(new Token(new TokenType("NONTERMINAL", Config.GRAMMAR_ARGUMENT_SYMBOL, 0, false), Config.GRAMMAR_ARGUMENT_SYMBOL));
    }


    /**
     * Test the {@link SLRParser#createParseTable()} with the grammar from {@link #setUp1()}.
     * @throws Exception If there was an error creating the parse table.
     */
    @Test
    @DisplayName("Test createParseTable()")
    public void testCreateParseTable() throws Exception {
        setUp1();

        final SLRParser parser = new SLRParser(grammar);
        final LRParseTable table = parser.createParseTable();
        final String expectTable = "SHIFT 4:ERROR 0:ERROR 0:ERROR 0:SHIFT 5:ERROR 0:;ERROR 0:ERROR 0:ERROR 0:SHIFT 6:ERROR 0:ACCEPT 0:;ERROR 0:REDUCE 2:SHIFT 7:REDUCE 2:ERROR 0:REDUCE 2:;ERROR 0:REDUCE 4:REDUCE 4:REDUCE 4:ERROR 0:REDUCE 4:;SHIFT 4:ERROR 0:ERROR 0:ERROR 0:SHIFT 5:ERROR 0:;ERROR 0:REDUCE 6:REDUCE 6:REDUCE 6:ERROR 0:REDUCE 6:;SHIFT 4:ERROR 0:ERROR 0:ERROR 0:SHIFT 5:ERROR 0:;SHIFT 4:ERROR 0:ERROR 0:ERROR 0:SHIFT 5:ERROR 0:;ERROR 0:SHIFT 11:ERROR 0:SHIFT 6:ERROR 0:ERROR 0:;ERROR 0:REDUCE 1:SHIFT 7:REDUCE 1:ERROR 0:REDUCE 1:;ERROR 0:REDUCE 3:REDUCE 3:REDUCE 3:ERROR 0:REDUCE 3:;ERROR 0:REDUCE 5:REDUCE 5:REDUCE 5:ERROR 0:REDUCE 5:;1:2:3:0:;0:0:0:0:;0:0:0:0:;0:0:0:0:;8:2:3:0:;0:0:0:0:;0:9:3:0:;0:0:10:0:;0:0:0:0:;0:0:0:0:;0:0:0:0:;0:0:0:0:;";
        
        assertEquals(expectTable, table.print());
    }


    /**
     * Test the {@link SLRParser#createParseTable()} with the grammar from {@link #setUp2()}.
     * Many unambiguous grammars are not SLR(1) (this test should be a positive exception test).
     * It should throw a {@link NoSLR1GrammarException}.
     * @throws Exception If there was an error creating the parse table.
     */
    @Test
    @DisplayName("Test createParseTable()")
    public void testCreateParseTable2() throws Exception {
        setUp2();

        final SLRParser parser = new SLRParser(grammar);
        assertThrows(NoSLR1GrammarException.class, () -> parser.createParseTable());
    }
}
