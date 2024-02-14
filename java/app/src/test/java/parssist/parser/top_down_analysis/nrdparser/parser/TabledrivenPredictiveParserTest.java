package parssist.parser.top_down_analysis.nrdparser.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NoLL1GrammarException;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NonRecursivePredictiveParseException;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Grammar;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Production;


/**
 * Testclass for {@link TabledrivenPredictiveParser}.
 */
public class TabledrivenPredictiveParserTest {
    private Grammar grammar1;
    private Grammar grammar2;
    private Grammar grammar3;


    /**
     * Set up the grammar, by using the following grammar:
     * E -> TE'
     * E' -> +TE' | $
     * T -> FT'
     * T' -> *FT' | $
     * F -> (E) | id
     * (Example 4.17 from the {@link <a href="https://suif.stanford.edu/dragonbook/">Dragonbook</a>})
     */
    private void setUpGrammar1() {
        List<Production> productions = new ArrayList<>();
        List<Token> vocabulary = new ArrayList<>();
        List<Token> alphabet = new ArrayList<>();
        List<Token[]> list1 = new ArrayList<>();
        List<Token[]> list2 = new ArrayList<>();
        List<Token[]> list3 = new ArrayList<>();
        List<Token[]> list4 = new ArrayList<>();
        List<Token[]> list5 = new ArrayList<>();
        

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

        Token startsymbol = new Token(
            new TokenType("NONTERMINAL", "", 0, false), "E"
        );
        
        List tokentypes = new ArrayList<>();
        tokentypes.add(new TokenType("TERMINAL", "\\(|\\)|\\*|\\+|id", 0, false));
        tokentypes.add(new TokenType("NONTERMINAL", "E|E_|T|T_|F", 0, false));
        tokentypes.add(new TokenType("EMPTY_SYMBOL", "\\$", 0, true));

        grammar1 = new Grammar(tokentypes, vocabulary, alphabet, productions, startsymbol, true);
    }

    /**
     * Set up the grammar, by using the following grammar:
     * S -> iEtSS' | a
     * S' -> eS | $
     * E -> b
     * (Example 4.19 from the {@link <a href="https://suif.stanford.edu/dragonbook/">Dragonbook</a>})
     */
    private void setUpGrammar2() {
        List<Production> productions = new ArrayList<>();
        List<Token> vocabulary = new ArrayList<>();
        List<Token> alphabet = new ArrayList<>();
        List<Token[]> list1 = new ArrayList<>();
        List<Token[]> list2 = new ArrayList<>();
        List<Token[]> list3 = new ArrayList<>();
        

        list1.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "i"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E"),
            new Token(new TokenType("TERMINAL", "", 0, false), "t"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "S"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "S_")
        });
        list1.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "a")
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
            new Token(new TokenType("TERMINAL", "", 0, false), "e"),
            new Token(new TokenType("NONTERMINAL", "", 0, false), "S")
        });
        list2.add(new Token[] {
            new Token(new TokenType("EMPTY_SYMBOL", "", 0, false), "$")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "S_"
                ), 
                list2
            )
        );


        list3.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "b")
        });
        productions.add(
            new Production(
                new Token(
                    new TokenType("NONTERMINAL", "", 0, false), "E"
                ), 
                list3
            )
        );


        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "S"
        ));
        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "S_"
        ));
        vocabulary.add(new Token(
            new TokenType("NONTERMINAL", "", 0, false), "E"
        ));
        
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "a"
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "b"
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "e"
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "i"
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "", 0, false), "t"
        ));
        alphabet.add(new Token(
            new TokenType("EMPTY_SYMBOL", "", 0, false), "$"
        ));

        Token startsymbol = new Token(
            new TokenType("NONTERMINAL", "", 0, false), "S"
        );
        

        grammar2 = new Grammar(new ArrayList<>(), vocabulary, alphabet, productions, startsymbol, true);
    }

    /**
     * Set up the grammar, by using the following grammar:
     * S -> TS | $
     * T -> (S) | $
     * (LL(1) Grammar for the language of balanced parentheses)
     */
    private void setUpGrammar3() {
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
            new TokenType("TERMINAL", "\\(", 0, false), "("
        ));
        alphabet.add(new Token(
            new TokenType("TERMINAL", "\\)", 0, false), ")"
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


        grammar3 = new Grammar(tokentypes, vocabulary, alphabet, productions, startsymbol, true);
    }


    /**
     * Test the {@link TabledrivenPredictiveParser#createParseTable(Grammar)} method with {@link TabledrivenPredictiveParserTest#setUpGrammar1()}.
     */
    @Test
    @DisplayName("Test with grammar 1")
    public void testCreateParseTableGrammar1() {
        setUpGrammar1();

        final TabledrivenPredictiveParser parser = new TabledrivenPredictiveParser(grammar1);

        List<Production>[][] parseTable = parser.createParseTable(grammar1);
        List<Token[]> list1 = new ArrayList<>();
        List<Token[]> list2_1 = new ArrayList<>();
        List<Token[]> list2_2 = new ArrayList<>();
        List<Token[]> list3 = new ArrayList<>();
        List<Token[]> list4_1 = new ArrayList<>();
        List<Token[]> list4_2 = new ArrayList<>();
        List<Token[]> list5_1 = new ArrayList<>();
        List<Token[]> list5_2 = new ArrayList<>();
        list1.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E_")
        });
        list2_1.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "+"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T"),
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E_")
        });
        list2_2.add(new Token[] {
            new Token(new TokenType("EMPTY_SYMBOL", "", 0, false), "$")
        });
        list3.add(new Token[] {
            new Token(new TokenType("NONTERMINAL", "", 0, false), "F"),
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T_")
        });
        list4_1.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "*"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "F"),
            new Token(new TokenType("NONTERMINAL", "", 0, false), "T_")
        });
        list4_2.add(new Token[] {
            new Token(new TokenType("EMPTY_SYMBOL", "", 0, false), "$")
        });
        list5_1.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "("),
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E"),
            new Token(new TokenType("TERMINAL", "", 0, false), ")")
        }
        );
        list5_2.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "id")
        });

        assertEquals(parseTable[0][0].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "E"), list1));
        assertEquals(parseTable[0][4].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "E"), list1));
        assertEquals(parseTable[0][0].size(), 1);
        assertEquals(parseTable[0][1].size(), 0);
        assertEquals(parseTable[0][2].size(), 0);
        assertEquals(parseTable[0][3].size(), 0);
        assertEquals(parseTable[0][4].size(), 1);
        assertEquals(parseTable[0][5].size(), 0);

        assertEquals(parseTable[1][1].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "E_"), list2_2));
        assertEquals(parseTable[1][3].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "E_"), list2_1));
        assertEquals(parseTable[1][5].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "E_"), list2_2));
        assertEquals(parseTable[1][0].size(), 0);
        assertEquals(parseTable[1][1].size(), 1);
        assertEquals(parseTable[1][2].size(), 0);
        assertEquals(parseTable[1][3].size(), 1);
        assertEquals(parseTable[1][4].size(), 0);
        assertEquals(parseTable[1][5].size(), 1);

        assertEquals(parseTable[2][0].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "T"), list3));
        assertEquals(parseTable[2][4].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "T"), list3));
        assertEquals(parseTable[2][0].size(), 1);
        assertEquals(parseTable[2][1].size(), 0);
        assertEquals(parseTable[2][2].size(), 0);
        assertEquals(parseTable[2][3].size(), 0);
        assertEquals(parseTable[2][4].size(), 1);
        assertEquals(parseTable[2][5].size(), 0);

        assertEquals(parseTable[3][1].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "T_"), list4_2));
        assertEquals(parseTable[3][2].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "T_"), list4_1));
        assertEquals(parseTable[3][3].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "T_"), list4_2));
        assertEquals(parseTable[3][5].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "T_"), list4_2));
        assertEquals(parseTable[3][0].size(), 0);
        assertEquals(parseTable[3][1].size(), 1);
        assertEquals(parseTable[3][2].size(), 1);
        assertEquals(parseTable[3][3].size(), 1);
        assertEquals(parseTable[3][4].size(), 0);
        assertEquals(parseTable[3][5].size(), 1);

        assertEquals(parseTable[4][0].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "F"), list5_1));
        assertEquals(parseTable[4][4].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "F"), list5_2));
        assertEquals(parseTable[4][0].size(), 1);
        assertEquals(parseTable[4][1].size(), 0);
        assertEquals(parseTable[4][2].size(), 0);
        assertEquals(parseTable[4][3].size(), 0);
        assertEquals(parseTable[4][4].size(), 1);
        assertEquals(parseTable[4][5].size(), 0);
    }


    /**
     * Test the {@link TabledrivenPredictiveParser#createParseTable(Grammar)} method with {@link TabledrivenPredictiveParserTest#setUpGrammar2()}.
     */
    @Test
    @DisplayName("Test with grammar 2")
    public void testCreateParseTableGrammar2() {
        setUpGrammar2();
        final TabledrivenPredictiveParser parser = new TabledrivenPredictiveParser(grammar2);

        List<Production>[][] parseTable = parser.createParseTable(grammar2);
        List<Token[]> list1_1 = new ArrayList<>();        
        List<Token[]> list1_2 = new ArrayList<>();
        List<Token[]> list2_1 = new ArrayList<>();
        List<Token[]> list2_2 = new ArrayList<>();
        List<Token[]> list3 = new ArrayList<>();
        list1_1.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "i"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "E"),
            new Token(new TokenType("TERMINAL", "", 0, false), "t"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "S"), 
            new Token(new TokenType("NONTERMINAL", "", 0, false), "S_")
        });
        list1_2.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "a")
        });
        list2_1.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "e"),
            new Token(new TokenType("NONTERMINAL", "", 0, false), "S")
        });
        list2_2.add(new Token[] {
            new Token(new TokenType("EMPTY_SYMBOL", "", 0, false), "$")
        });
        list3.add(new Token[] {
            new Token(new TokenType("TERMINAL", "", 0, false), "b")
        });

        assertEquals(parseTable[0][0].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "S"), list1_2));
        assertEquals(parseTable[0][3].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "S"), list1_1));
        assertEquals(parseTable[0][0].size(), 1);
        assertEquals(parseTable[0][1].size(), 0);
        assertEquals(parseTable[0][2].size(), 0);
        assertEquals(parseTable[0][3].size(), 1);
        assertEquals(parseTable[0][4].size(), 0);
        assertEquals(parseTable[0][5].size(), 0);

        assertEquals(parseTable[1][2].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "S_"), list2_1));
        assertEquals(parseTable[1][2].get(1), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "S_"), list2_2));
        assertEquals(parseTable[1][5].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "S_"), list2_2));
        assertEquals(parseTable[1][0].size(), 0);
        assertEquals(parseTable[1][1].size(), 0);
        assertEquals(parseTable[1][2].size(), 2);
        assertEquals(parseTable[1][3].size(), 0);
        assertEquals(parseTable[1][4].size(), 0);
        assertEquals(parseTable[1][5].size(), 1);

        assertEquals(parseTable[2][1].get(0), new Production(new Token(new TokenType("NONTERMINAL", "", 0, false), "E"), list3));
        assertEquals(parseTable[2][0].size(), 0);
        assertEquals(parseTable[2][1].size(), 1);
        assertEquals(parseTable[2][2].size(), 0);
        assertEquals(parseTable[2][3].size(), 0);
        assertEquals(parseTable[2][4].size(), 0);
        assertEquals(parseTable[2][5].size(), 0);
    }


    /**
     * Test the {@link TabledrivenPredictiveParser#createParseTable(Grammar)} method with {@link TabledrivenPredictiveParserTest#setUpGrammar3()}.
     * @throws NonRecursivePredictiveParseException If there is an exception while parsing the input.
     * @throws NoLL1GrammarException If the grammar is not LL(1). 
     */
    @Test
    @DisplayName("Test with grammar 3")
    public void testComputeSystemAnalysisGrammar3() throws NonRecursivePredictiveParseException, NoLL1GrammarException {
        setUpGrammar3();

        final TabledrivenPredictiveParser parser = new TabledrivenPredictiveParser(grammar3, "()");

        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("(()())");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("$");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("(()(()))");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("(()(()))()");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("(()(()))()()");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("(()(()))()()()");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("$$$$$$");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("(($$$$)(($$)))()()");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("((($$$$)(($$)))()())");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("((($$$$)(($$)))()()");
        parser.resetStack();
        assertThrows(NonRecursivePredictiveParseException.class, () -> parser.computeSystemAnalysis());

        parser.setInputString("((($$$$)(($$)))()()))");
        parser.resetStack();
        assertThrows(NonRecursivePredictiveParseException.class, () -> parser.computeSystemAnalysis());

        parser.setInputString("((($$$$)(($$)))(a)())");
        parser.resetStack();
        assertThrows(NonRecursivePredictiveParseException.class, () -> parser.computeSystemAnalysis());

        parser.setInputString("())");
        parser.resetStack();
        assertThrows(NonRecursivePredictiveParseException.class, () -> parser.computeSystemAnalysis());
    }


    /**
     * Test the {@link TabledrivenPredictiveParser#createParseTable(Grammar)} method with {@link TabledrivenPredictiveParserTest#setUpGrammar2()}.
     * @throws NonRecursivePredictiveParseException If there is an exception while parsing the input.
     * @throws NoLL1GrammarException If the grammar is not LL(1). 
     */
    @Test
    @DisplayName("Test with grammar 2")
    public void testComputeSystemAnalysisGrammar2() throws NonRecursivePredictiveParseException, NoLL1GrammarException {
        setUpGrammar2();

        final TabledrivenPredictiveParser parser = new TabledrivenPredictiveParser(grammar2, "ibta$");

        assertThrows(NoLL1GrammarException.class, () -> parser.computeSystemAnalysis());
    }


    /**
     * Test the {@link TabledrivenPredictiveParser#createParseTable(Grammar)} method with {@link TabledrivenPredictiveParserTest#setUpGrammar1()}.
     * @throws NonRecursivePredictiveParseException If there is an exception while parsing the input.
     * @throws NoLL1GrammarException If the grammar is not LL(1). 
     */
    @Test
    @DisplayName("Test with grammar 1")
    public void testComputeSystemAnalysisGrammar1() throws NonRecursivePredictiveParseException, NoLL1GrammarException {
        setUpGrammar1();

        final TabledrivenPredictiveParser parser = new TabledrivenPredictiveParser(grammar1, "id");

        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("id+id");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("id+$id");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("id+id*id");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("id+id*id+id");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("id+id*id+id*id");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("id+id*id+id*id+id");
        parser.resetStack();
        assertTrue(parser.computeSystemAnalysis());

        parser.setInputString("id+id*id+id*id+id*");
        parser.resetStack();
        assertThrows(NonRecursivePredictiveParseException.class, () -> parser.computeSystemAnalysis());

        parser.setInputString("id++id");
        parser.resetStack();
        assertThrows(NonRecursivePredictiveParseException.class, () -> parser.computeSystemAnalysis());

        parser.setInputString("+id+id");
        parser.resetStack();
        assertThrows(NonRecursivePredictiveParseException.class, () -> parser.computeSystemAnalysis());

        parser.setInputString("*");
        parser.resetStack();
        assertThrows(NonRecursivePredictiveParseException.class, () -> parser.computeSystemAnalysis());

        parser.setInputString("$+id+id");
        parser.resetStack();
        assertThrows(NonRecursivePredictiveParseException.class, () -> parser.computeSystemAnalysis());
    }
}
