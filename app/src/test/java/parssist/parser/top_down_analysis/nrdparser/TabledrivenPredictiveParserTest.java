package parssist.parser.top_down_analysis.nrdparser;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.top_down_analysis.nrdparser.util.Grammar;
import parssist.parser.top_down_analysis.nrdparser.util.Production;


/**
 * Testclass for {@link TabledrivenPredictiveParser}.
 */
public class TabledrivenPredictiveParserTest {
    private List<Production> productions;
    private List<Token> vocabulary; 
    private List<Token> alphabet;
    private Token startsymbol;
    private Grammar grammar;
    private List<Token[]> list1;
    private List<Token[]> list2;
    private List<Token[]> list3;
    private List<Token[]> list4;
    private List<Token[]> list5;


    /**
     * Set up the grammar, by using the following grammar:
     * E -> TE'
     * E' -> +TE' | $
     * T -> FT'
     * T' -> *FT' | $
     * F -> (E) | id
     * (Example 4.17 from the {@link <a href="https://suif.stanford.edu/dragonbook/">Dragonbook</a>})
     */
    @BeforeEach public void setUp() {
        productions = new ArrayList<>();
        vocabulary = new ArrayList<>();
        alphabet = new ArrayList<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        list4 = new ArrayList<>();
        list5 = new ArrayList<>();
        

        list1.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E_")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "E"
                ), 
                list1
            )
        );


        list2.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "+"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T"),
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E_")
        });
        list2.add(new Token[] {
            new Token(new TokenType("EMPTY_SYMBOL", "", 0, false), "$")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "E_"
                ), 
                list2
            )
        );


        list3.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "F"),
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T_")
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
            new Token(new TokenType("TERMINAL", "", 0, false), "*"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "F"),
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T_")
        });
        list4.add(new Token[] {
            new Token(new TokenType("EMPTY_SYMBOL", "", 0, false), "$")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "T_"
                ), 
                list4
            )
        );


        list5.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "("),
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E"),
            new Token(new TokenType("TERMINAL", "", 0, false), ")")
        }
        );
        list5.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "id")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "F"
                ), 
                list5
            )
        );


        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "E"
        ));
        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "E_"
        ));
        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "T"
        ));
        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "T_"
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
    }


    /**
     * Test the {@link TabledrivenPredictiveParser#createParseTable(Grammar)} method.
     */
    @Test
    public void testCreateParseTable() {
        final TabledrivenPredictiveParser parser = new TabledrivenPredictiveParser(grammar);

        List<Production>[][] parseTable = parser.createParseTable(grammar);

        // print parse table
        System.out.println("Parse table:");
        for(int i = 0; i < parseTable.length; i++) {
            for(int j = 0; j < parseTable[i].length; j++) {
                System.out.print("[" + i + "][" + j + "]: ");
                for(int k = 0; k < parseTable[i][j].size(); k++) {
                    System.out.print(parseTable[i][j].get(k) + " ");
                }
                System.out.println();
            }
        }
    }
}
