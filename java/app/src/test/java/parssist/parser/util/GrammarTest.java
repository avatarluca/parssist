package parssist.parser.util;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import parssist.Config;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.util.Grammar.Item;


/**
 * Testclass for {@link Grammar}.
 */
public class GrammarTest {
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
    public void setUp1() {
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
        

        grammar = new Grammar(new ArrayList<>(), vocabulary, alphabet, productions, startsymbol, false);
    }

    /**
     * Set up the grammar, by using the following grammar:
     * E_ -> E
     * E -> E + T | T
     * T -> T*F | F
     * F -> (E) | id
     * (Example 4.19 from the {@link <a href="https://suif.stanford.edu/dragonbook/">Dragonbook</a>})
     */
    public void setUp2() {
        productions = new ArrayList<>();
        vocabulary = new ArrayList<>();
        alphabet = new ArrayList<>();
        list1 = new ArrayList<>();
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

        startsymbol = new Token(
            new TokenType("NONTERMINAL", "", 0, false), "E"
        );
        

        grammar = new Grammar(new ArrayList<>(), vocabulary, alphabet, productions, startsymbol, true);
        grammar.addArgumentProduction(new Token(new TokenType("NONTERMINAL", Config.GRAMMAR_ARGUMENT_SYMBOL, 0, false), Config.GRAMMAR_ARGUMENT_SYMBOL));
    }


    /**
     * Test the first function. Maybe it is better to compute the most below non terminal first, because then the upper ones are already in the set.
     */
    @Test
    @DisplayName("Test the FIRST function.")
    public void testFirstIntegration() {       
        setUp1(); 

        String first_e = grammar.first("E").stream().map(e->((Token) e).symbol()).reduce("", (a, b) -> a + b);
        String first_e_ = grammar.first("E_").stream().map(e->((Token) e).symbol()).reduce("", (a, b) -> a + b);
        String first_t = grammar.first("T").stream().map(e->((Token) e).symbol()).reduce("", (a, b) -> a + b);
        String first_t_ = grammar.first("T_").stream().map(e->((Token) e).symbol()).reduce("", (a, b) -> a + b);
        String first_f = grammar.first("F").stream().map(e->((Token) e).symbol()).reduce("", (a, b) -> a + b);


        assert(first_e.equals("id("));
        assert(first_e_.equals("+$"));
        assert(first_t.equals("id("));
        assert(first_t_.equals("*$"));
        assert(first_f.equals("id("));
    }

    
    /**
     * Test the follow function.
     */
    @Test
    @DisplayName("Test the FOLLOW function.")
    public void testFollowIntegration() {       
        setUp1(); 

        String follow_e = grammar.follow("E", "E").stream().map(e->((Token) e).symbol()).reduce("", (a, b) -> a + b);
        String follow_e_ = grammar.follow("E_", "E_").stream().map(e->((Token) e).symbol()).reduce("", (a, b) -> a + b);
        String follow_t = grammar.follow("T", "T").stream().map(e->((Token) e).symbol()).reduce("", (a, b) -> a + b);
        String follow_t_ = grammar.follow("T_", "T_").stream().map(e->((Token) e).symbol()).reduce("", (a, b) -> a + b);
        String follow_f = grammar.follow("F", "F").stream().map(e->((Token) e).symbol()).reduce("", (a, b) -> a + b);


        assert(follow_e.equals("$)"));
        assert(follow_e_.equals("$)"));
        assert(follow_t.equals("$)+"));
        assert(follow_t_.equals("$)+"));
        assert(follow_f.equals("$)*+"));
    }


    /**
     * Test the closure function.
     */
    @Test 
    @DisplayName("Test the CLOSURE function.")
    public void testClosureIntegration() {
        setUp2(); 

        final Token lhs = new Token(new TokenType("NONTERMINAL", Config.GRAMMAR_ARGUMENT_SYMBOL, 0, false), Config.GRAMMAR_ARGUMENT_SYMBOL);
        final List<Token[]> rhs = new ArrayList<>();
        rhs.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "E", 0, false), "E")
        });
        final Production production = new Production(lhs, rhs);

        final List<Item> I = new ArrayList<>();
        I.add(new Item(production, 0));

        final List<Item> closure = grammar.closure(I);

        final String actualClosure = closure.stream().map(e->e.toString()).reduce("", (a, b) -> a + "\n" + b);
        final String expectedClosure = "\n" + Config.GRAMMAR_ARGUMENT_SYMBOL + " -> .E\nE -> .E+T\nE -> .T\nT -> .T*F\nT -> .F\nF -> .(E)\nF -> .id";

        assertEquals(expectedClosure, actualClosure);
    }


    /**
     * Test the goto function.
     */
    @Test
    @DisplayName("Test the GOTO function.")
    public void testGotoIntegration() {
        setUp2(); 

        final Token lhs1 = new Token(new TokenType("NONTERMINAL", Config.GRAMMAR_ARGUMENT_SYMBOL, 0, false), Config.GRAMMAR_ARGUMENT_SYMBOL);
        final List<Token[]> rhs1 = new ArrayList<>();
        rhs1.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E")
        });
        final Production production1 = new Production(lhs1, rhs1);

        final Token lhs2 = new Token(new TokenType("NONTERMINAL", "", 0, false), "E");
        final List<Token[]> rhs2 = new ArrayList<>();
        rhs2.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E"),
            new Token(new TokenType("TERMINAL", "", 0, false), "+"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T")
        });

        final Production production2 = new Production(lhs2, rhs2);

        final List<Item> I = new ArrayList<>();
        I.add(new Item(production1, 1));
        I.add(new Item(production2, 1));
        
        final List<Item> goto_ = grammar.goTo(I, new Token(new TokenType("TERMINAL", "", 0, false), "+"));

        final String actualGoto = goto_.stream().map(e->e.toString()).reduce("", (a, b) -> a + "\n" + b);
        final String expectedGoto = "\nE -> E+.T\nT -> .T*F\nT -> .F\nF -> .(E)\nF -> .id";

        assertEquals(expectedGoto, actualGoto);
    }
}