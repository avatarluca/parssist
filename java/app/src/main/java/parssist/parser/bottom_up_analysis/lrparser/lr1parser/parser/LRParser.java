package parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser;

import java.util.ArrayList;
import java.util.List;

import parssist.Config;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.Parser;
import parssist.parser.util.Grammar;
import parssist.parser.util.Grammar.Item;
import parssist.parser.util.tree.ParseTreeNode;


/**
 * A non recursive shift-reduce parser.
 */
abstract public class LRParser extends Parser {
    protected final Grammar grammar;


    /**
     * Create a new LR parser.
     * @param grammar The grammar to parse.
     */
    public LRParser(final Grammar grammar) {
        this.grammar = grammar;
        grammar.addArgumentProduction(new Token(new TokenType(Config.GRAMMAR_ARGUMENT_SYMBOL, Config.GRAMMAR_ARGUMENT_SYMBOL, 0, false), Config.GRAMMAR_ARGUMENT_SYMBOL));
    }

    


    @Override public ParseTreeNode parse(String w) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parse'");
    }
    

    /**
     * Create a parse (action-goto-)table for the given grammar.
     * The action table is a 2D array, where the first index is the state and the second index is the terminal symbol.
     * The goto table is a 2D array, where the first index is the state and the second index is the non-terminal symbol.
     * @return The parse table.
     * @throw Exception if there was an error creating the parse table.
     */
    abstract protected LRParseTable createParseTable() throws Exception;


    /**
     * A single action in the parse table.
     */
    protected static class Action {
        public static enum Type {
            SHIFT, 
            REDUCE, 
            ACCEPT,
            ERROR
        }


        public final Type type;
        public final int value;


        /**
         * Create a new action.
         * @param type The type of the action.
         * @param value The value of the action.
         */
        public Action(Type type, int value) {
            this.type = type;
            this.value = value;
        }


        @Override public String toString() {
            return type + " " + value;
        }
    }

    /**
     * A parse table for the LR parser.
     * Represents mainly the action and goto table.
     */
    protected static class LRParseTable {
        public final Grammar grammar;
        public final Action[][] actionTable;
        public final int[][] gotoTable;
        public final List<List<Item>> states;
        public final List<Token> alphabet;
        public final List<Token> vocabulary;
        public final List<Integer> startStates;


        /**
         * Create a new parse table.
         * @param actionTable The action table.
         * @param gotoTable The goto table.
         * @param states The states of the parser.
         * @param alphabet The alphabet of the grammar.
         * @param vocabulary The vocabulary of the grammar.
         */
        public LRParseTable(final Grammar grammar, final Action[][] actionTable, final int[][] gotoTable, final List<List<Item>> states, final List<Token> alphabet, final List<Token> vocabulary) {
            this.grammar = grammar;
            this.actionTable = actionTable;
            this.gotoTable = gotoTable;
            this.states = states;
            this.alphabet = alphabet;
            this.vocabulary = vocabulary;
            this.startStates = new ArrayList<>();

            for(List<Item> state : states) {
                for(Item item : state) {
                    if(item.getProduction().getLhs().symbol().equals(Config.GRAMMAR_ARGUMENT_SYMBOL) 
                    && item.getPosition() == 0
                    && grammar.isSymbolNonTerminal(item.getProduction().getRhs().get(0)[0].symbol())) {
                        startStates.add(states.indexOf(state));
                        break;
                    }
                }
            }
        }   


        @Override public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("Action Table:\n");
            for(int i = 0; i < actionTable.length; i++) {
                sb.append("State " + i + ":\n");
                for(int j = 0; j < actionTable[i].length; j++) {
                    sb.append("  " + alphabet.get(j).symbol() + ": " + actionTable[i][j] + "\n");
                }
            }

            sb.append("Goto Table:\n");
            for(int i = 0; i < gotoTable.length; i++) {
                sb.append("State " + i + ":\n");
                for(int j = 0; j < gotoTable[i].length; j++) {
                    sb.append("  " + vocabulary.get(j).symbol() + ": " + gotoTable[i][j] + "\n");
                }
            }

            return sb.toString();
        }


        public Action[][] getActionTable() {
            return actionTable;
        }

        public int[][] getGotoTable() {
            return gotoTable;
        }

        public List<List<Item>> getStates() {
            return states;
        }

        public List<Token> getAlphabet() {
            return alphabet;
        }

        public List<Token> getVocabulary() {
            return vocabulary;
        }

        public List<Integer> getStartStates() {
            return startStates;
        }


        /**
         * Prepare the short version of the parse table.
         * @return The short version of the parse table.
         */
        public String print() {
            String res = "";
            for(int i = 0; i < actionTable.length; i++) {
                for(int j = 0; j < actionTable[i].length; j++) {
                    res += actionTable[i][j] + ":";
                }
                res += ";";
            }
            for(int i = 0; i < gotoTable.length; i++) {
                for(int j = 0; j < gotoTable[i].length; j++) {
                    res += gotoTable[i][j] + ":";
                }
                res += ";";
            }

            return res;
        }
    }
}
