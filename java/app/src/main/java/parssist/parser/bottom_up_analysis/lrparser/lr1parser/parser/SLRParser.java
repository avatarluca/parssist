package parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser;

import parssist.Config;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.exception.NoSLR1GrammarException;
import parssist.parser.util.Grammar;
import parssist.parser.util.Grammar.Item;

import java.util.List;
import java.util.Set;


/**
 * A simple LR parser implementation, which can create a SLR parsetable.
 */
public class SLRParser extends LRParser {
    /**
     * Creates a new SLR parser.
     * @param grammar The grammar to parse.
     * @param w The input string.
     */
    public SLRParser(final Grammar grammar, final String w) {
        super(grammar, w);
    }

    /**
     * Creates a SLR parse table with an empty input string.
     * @param grammar The grammar to parse.
     */
    public SLRParser(final Grammar grammar) {
        super(grammar, "");
    }


    /**
     * Creates a SLR parse table.
     * Startstate of the parser is the set of items of the augmented grammar with the item [S' -> .S].
     * @throws NoSLR1GrammarException if the grammar is not SLR(1). Its not possible to create a SLR(1) parse table for the given grammar.
     */
    @Override public LRParseTable createParseTable() throws NoSLR1GrammarException {        
        final List<List<Item>> C = grammar.elements();
        final Action[][] actions = new Action[C.size()][grammar.getAlphabet().size()];
        final int[][] gotos = new int[C.size()][grammar.getVocabulary().size()];

        for(int i = 0; i < C.size(); i++) {
            for(final Item item : C.get(i)) {
                for(int j = 0; j < C.size(); j++) {
                    if(isShiftPossible(item, C.get(i), C.get(j))) {
                        Token token = null;

                        if(item.getPosition() >= item.getProduction().getRhs().get(0).length) token = new Token(new TokenType(Config.LEXER_EMPTY_SYMBOL, "", 0, false), Grammar.EMPTY_SYMBOL);
                        else token = item.getProduction().getRhs().get(0)[item.getPosition()];

                        final int a = grammar.getAlphabet().indexOf(token);
                        if(a >= 0) {
                            if(actions[i][a] != null && actions[i][a].type != Action.Type.ERROR && actions[i][a].type != Action.Type.SHIFT) throw new NoSLR1GrammarException(Config.BOTTOM_UP_PARSER_ERROR_NO_SLR1_GRAMMAR);

                            actions[i][a] = new Action(Action.Type.SHIFT, j);
                        }
                    } 

                    for(final Token A : grammar.getVocabulary()) {
                        final List<Item> $Ij = grammar.goTo(C.get(i), A);

                        boolean equals = true;

                        if($Ij.size() == C.get(j).size()) {
                            for(final Item $item : C.get(j)) {
                                if(!$Ij.contains($item)) {
                                    equals = false;
                                    break;
                                }
                            }
                        } else equals = false;

                        if(equals) gotos[i][grammar.getVocabulary().indexOf(A)] = j;
                    }
                }
                if(isReductionPossible(item, C.get(i))) {
                    final Set<Token> follow = grammar.follow(item.getProduction().getLhs().symbol(), item.getProduction().getLhs().symbol());

                    for(final Token token : follow) {
                        final int a = grammar.getAlphabet().indexOf(token);

                        if(a >= 0) {
                            if(actions[i][a] != null && actions[i][a].type != Action.Type.ERROR && actions[i][a].type != Action.Type.REDUCE) throw new NoSLR1GrammarException(Config.BOTTOM_UP_PARSER_ERROR_NO_SLR1_GRAMMAR);
                            actions[i][a] = new Action(Action.Type.REDUCE, grammar.getProductions().indexOf(item.getProduction()));
                        }
                    }
                } else if(isAcceptPossible(item, C.get(i))) {
                    final int a = grammar.getAlphabet().indexOf(new Token(new TokenType(Config.LEXER_EMPTY_SYMBOL, "", 0, false), Grammar.EMPTY_SYMBOL));

                    if(a >= 0) {
                        if(actions[i][a] != null && actions[i][a].type != Action.Type.ERROR && actions[i][a].type != Action.Type.ACCEPT) throw new NoSLR1GrammarException(Config.BOTTOM_UP_PARSER_ERROR_NO_SLR1_GRAMMAR);

                        actions[i][a] = new Action(Action.Type.ACCEPT, 0);
                    }
                }
            }
        }

        for(int i = 0; i < actions.length; i++) {
            for(int j = 0; j < actions[i].length; j++) {
                if(actions[i][j] == null) actions[i][j] = new Action(Action.Type.ERROR, 0);
            }
        }

        return new LRParseTable(grammar, actions, gotos, C, grammar.getAlphabet(), grammar.getVocabulary());
    }


    /**
     * Check if shift is possible.
     * @param item The current item.
     * @param Ii The current set of items.
     * @param Ij The next set of items.
     * @return True if shift is possible, false otherwise.
     */
    private boolean isShiftPossible(final Item item, final List<Item> Ii, final List<Item> Ij) {
        Token a = null;
        boolean jumpState = true;

        if(item.getPosition() >= item.getProduction().getRhs().get(0).length) a = new Token(new TokenType(Config.LEXER_EMPTY_SYMBOL, "", 0, false), Grammar.EMPTY_SYMBOL);
        else a = item.getProduction().getRhs().get(0)[item.getPosition()];

        if(!grammar.isSymbolTerminal(a.symbol())) return false;

        final List<Item> goto_Ii = grammar.goTo(Ii, a);

        if(Ij.size() == goto_Ii.size()) {
            for(final Item $item : Ij) {
                if(!goto_Ii.contains($item)) {
                    jumpState = false;
                    break;
                }
            }
        } else jumpState = false;

        return Ii.contains(item) && jumpState;
    }

    /**
     * Check if reduction is possible.
     * @param item The current item.
     * @param Ii The current set of items.
     * @return True if reduction is possible, false otherwise.
     */
    private boolean isReductionPossible(final Item item, final List<Item> Ii) {
        final Token lhs = item.getProduction().getLhs();
        
        return Ii.contains(item) && !lhs.equals(grammar.getStartsymbol()) && (item.getPosition() == item.getProduction().getRhs().get(0).length || item.getProduction().getRhs().get(0)[item.getPosition()].symbol().equals(Grammar.EMPTY_SYMBOL));
    }

    /**
     * Check if accept is possible.
     * @param item The current item.
     * @param Ii The current set of items.
     * @return True if accept is possible, false otherwise.
     */
    private boolean isAcceptPossible(final Item item, final List<Item> Ii) {
        return Ii.contains(item) && item.getProduction().getLhs().equals(grammar.getStartsymbol()) && item.getPosition() == item.getProduction().getRhs().get(0).length;
    }
}
