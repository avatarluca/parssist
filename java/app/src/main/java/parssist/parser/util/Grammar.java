package parssist.parser.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import parssist.Config;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.top_down_analysis.nrdparser.parser.TabledrivenPredictiveParser;


/**
 * Grammar class which is used to describe a parsergrammar.
 * It uses a extended BNF format, which corresponds to the 4-tuple (V, A, P, S).
 * V is a set of non-terminals (vocabulary), A is the alphabet (including the empty symbol), P the production rules and S the start symbol.
 * The grammar takes a list of productions and optionally transforms it into a list of productions, where the rhs has only one symbol (see {@link Grammar#splitProductions(List)}).
 * This is needed for the {@link TabledrivenPredictiveParser}.
 */
public class Grammar {
    public static final String EMPTY_SYMBOL = "$";

    private final List<TokenType> tokentypes;
    private final List<Token> vocabulary;
    private final List<Token> alphabet;
    private final List<Production> productions;
    private final boolean preproc;

    private Token startsymbol;
    private boolean extended;


    /**
     * Creates a new BNF grammar.
     * @param tokentypes Tokentypes of the grammar.
     * @param vocabulary Vocabulary of the grammar.
     * @param alphabet Alphabet of the grammar.
     * @param productions Productionrules of the grammar.
     * @param startsymbol Startsymbol of the grammar.
     * @param preproc If true, the productions will be preprocessed (see {@link Grammar#splitProductions(List)}).
     * @throws NullPointerException If one argument is null.
     */
    public Grammar(final List<TokenType> tokentypes, final List<Token> vocabulary, final List<Token> alphabet, final List<Production> productions, final Token startsymbol, final boolean preproc) throws NullPointerException {
        Objects.requireNonNull(tokentypes);
        Objects.requireNonNull(vocabulary);
        Objects.requireNonNull(alphabet);
        Objects.requireNonNull(productions);
        Objects.requireNonNull(startsymbol);

        this.tokentypes = tokentypes;
        this.vocabulary = vocabulary;
        this.alphabet = alphabet;
        this.startsymbol = startsymbol;
        
        if(preproc) this.productions = splitProductions(productions);
        else this.productions = productions;

        this.preproc = preproc;
        this.extended = false;
    }


    public List<TokenType> getTokentypes() {
        return tokentypes;
    }

    public List<Token> getVocabulary() {
        return vocabulary;
    }

    public List<Token> getAlphabet() {
        return alphabet;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public Token getStartsymbol() {
        return startsymbol;
    }

    public boolean isPreproc() {
        return preproc;
    }

    public boolean isExtended() {
        return extended;
    }


    @Override public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("V := {")
        .append(vocabulary.stream().map(e -> e.symbol()).reduce((a, b) -> a + ", " + b).get())
        .append("}\n");
        sb.append("A := {")
        .append(alphabet.stream().map(e -> e.symbol()).reduce((a, b) -> a + ", " + b).get())
        .append("}\n");
        sb.append("P := {\n ")
        .append(productions.stream().map(e -> e.toString()).reduce((a, b) -> a + ",\n " + b).get())
        .append("\n}\n");
        sb.append("S := ").append(startsymbol.symbol()).append("\n");

        return sb.toString();
    }


    /**
     * FIRST(a) is the set of all terminal definitions with which a string derived from a can begin.
     * @param a The string (any sequence of grammatical symbols), which is used to get the first set.
     * @return The first set of the given string.
     * @throws IllegalArgumentException If the given string is not a symbol.
     */
    public Set<Token> first(final String a) throws IllegalArgumentException {
        Set<Token> result = new HashSet<>();

        if(isSymbolTerminal(a)) return Set.of(new Token(new TokenType(Config.LEXER_TERMINAL, a, 0, false), a));
        else if(isSymbolNonTerminal(a)) {
            for(Production production : productions) {
                if(production.getLhs().symbol().equals(a)) {
                    List<Token[]> rhs = production.getRhs();

                    for(Token[] rules : rhs) {
                        if(rules.length == 0) continue;
                        if(rules.length == 1 && rules[0].symbol().equals(Grammar.EMPTY_SYMBOL)) {
                            result.add(new Token(new TokenType(Config.LEXER_EMPTY_SYMBOL, Grammar.EMPTY_SYMBOL, 0, false), Grammar.EMPTY_SYMBOL));
                            continue;
                        }
                        
                        result.addAll(first(rules[0].symbol()));
                    }
                }
            }
        } else throw new IllegalArgumentException(Config.GRAMMAR_ERROR_INVALID_SYMBOL + a);
        
        return result;
    }

    /**
     * FOLLOW(A) is the set of all terminal definitions that can follow directly after A.
     * Startsymbol S always has {@link Grammar#EMPTY_SYMBOL} in FOLLOW(S), because {@link Grammar#EMPTY_SYMBOL} is the end of the input string.
     * @param a The symbol, which is used to get the follow set.
     * @param a_ The symbol, which is used to get the follow set. This is needed to prevent left recursion.
     * @return The follow set of the given symbol.
     * @throws IllegalArgumentException If the given symbol is not a non terminal symbol.
     */
    public Set<Token> follow(final String a, final String a_) throws IllegalArgumentException {
        Comparator<Token> tokenComparator = Comparator
                .comparing(Token::symbol)
                .thenComparing(e -> e.tokenType().name());
        Set<Token> result = new TreeSet<>(tokenComparator);
        
        if(a.equals(startsymbol.symbol())) result.add(new Token(new TokenType(Config.LEXER_EMPTY_SYMBOL, Grammar.EMPTY_SYMBOL, 0, false), Grammar.EMPTY_SYMBOL));

        if(isSymbolNonTerminal(a)) {
            for(Production production : productions) {
                List<Token[]> rhs = production.getRhs();
                
                for(Token[] rules : rhs) {
                    if(rules.length == 1 && rules[0].symbol().equals(Grammar.EMPTY_SYMBOL)) continue;
                    
                    for(int i = 0; i < rules.length; i++) {
                        if(rules[i].symbol().equals(a)) {
                            if(i == rules.length - 1) { // There isnt anything to follow => check if lhs != rhs
                                if(!production.getLhs().symbol().equals(a) && !production.getLhs().symbol().equals(a_)) result.addAll(follow(production.getLhs().symbol(), a_));
                            } 
                            else {
                                if(isSymbolNonTerminal(rules[i + 1].symbol())) {
                                    final Set<Token> first = first(rules[i + 1].symbol());

                                    for(Token token : first) {
                                        if(token.symbol().equals(Grammar.EMPTY_SYMBOL)) {
                                            if(i + 2 < rules.length) result.addAll(follow(rules[i + 2].symbol(), a_));
                                            else if(!production.getLhs().symbol().equals(a)) result.addAll(follow(production.getLhs().symbol(), a_));
                                        } else result.add(token);
                                    }
                                }
                                else if(isSymbolTerminal(rules[i + 1].symbol())) {
                                    result.add(new Token(new TokenType(Config.LEXER_TERMINAL, rules[i + 1].symbol(), 0, false), rules[i + 1].symbol()));
                                }
                            }
                        }
                    }
                }
            }
        } else throw new IllegalArgumentException(Config.GRAMMAR_ERROR_INVALID_SYMBOL + a);

        return result;
    }

    /**
     * Checks if the given symbol is a terminal.
     * @param symbol The symbol which is checked.
     * @return True if the symbol is a terminal, false otherwise.
     */
    public boolean isSymbolTerminal(final String symbol) {
        return alphabet.stream()
                        .map(e -> e.symbol())
                        .anyMatch(e -> e.equals(symbol))
            || alphabet.stream()
                        .map(e -> e.tokenType().regex())
                        .anyMatch(e -> symbol.matches(Config.LEXER_REGEX_STARTSYMBOL + e + Config.LEXER_REGEX_ENDSYMBOL));
    }

    /**
     * Checks if the given symbol is a non-terminal.
     * @param symbol The symbol which is checked.
     * @return True if the symbol is a non-terminal, false otherwise.
     */
    public boolean isSymbolNonTerminal(final String symbol) {
        return vocabulary.stream()
                        .map(e -> e.symbol())
                        .anyMatch(e -> e.equals(symbol))
            || vocabulary.stream()
                        .map(e -> e.tokenType().regex())
                        .anyMatch(e -> symbol.matches(Config.LEXER_REGEX_STARTSYMBOL + e + Config.LEXER_REGEX_ENDSYMBOL));
    }

    /**
     * Calculates CLOSURE(I) for the given itemset I.
     * It is the set of all items J such that (A -> a.Bb) is in J whenever (A -> a.Bb) is in I and b is in FIRST(b) and B -> .b is in I.
     * A item is represented as a 2 field object, where the first field is the production and the second field is the dot position in the rhs.
     * @param itemset The itemset for which the cover should be calculated.
     * @return The cover of the given itemset.
     * @throws IllegalArgumentException If the grammar is not preprocessed.
     */
    public List<Item> closure(final List<Item> I) {
        if(!isPreproc()) throw new IllegalArgumentException(Config.NONREC_PARSER_ERROR_PREPROCESSED);

        List<Item> J = new ArrayList<>();
        boolean changed = true;
        J.addAll(I);
    
        while(changed) {
            final List<Item> toAdd = new ArrayList<>();

            for(Item item : J) {
                Token $B = null;

                if(item.getPosition() >= item.getProduction().getRhs().get(0).length) $B = new Token(new TokenType(Config.LEXER_EMPTY_SYMBOL, Grammar.EMPTY_SYMBOL, 0, false), Grammar.EMPTY_SYMBOL);
                else $B = item.getProduction().getRhs().get(0)[item.getPosition()];
    
                if(isSymbolNonTerminal($B.symbol())) {
                    for(Production production : productions) {
                        final Item newItem = new Item(production, 0);
    
                        if(production.getLhs().symbol().equals($B.symbol())
                        && !J.contains(newItem)) toAdd.add(newItem);
                    }
                }
            }

            if(toAdd.size() < 1) changed = false;
            else J.addAll(toAdd);
        }

        return J;
    }

    /**
     * Calculates GOTO(I, X) for the given itemset I and symbol X.
     * Uses the start symbol S.
     * @param I The itemset.
     * @param X The symbol.
     * @return The goto of the given itemset and symbol.
     * @throws IllegalArgumentException If the grammar is not preprocessed.
     */
    public List<Item> goTo(final List<Item> I, final Token X) {
        if(!isPreproc()) throw new IllegalArgumentException(Config.NONREC_PARSER_ERROR_PREPROCESSED);

        List<Item> J = new ArrayList<>();

        for(Item item : I) {
            final Token[] rhs = item.getProduction().getRhs().get(0);
            final int position = item.getPosition();

            if(position < rhs.length && rhs[position].equals(X)) {
                J.add(new Item(item.getProduction(), position + 1));
            }
        }

        return closure(J);
    }

    /**
     * Calculates the canonical collection of LR(0) items for the grammar (it should be extended).
     * @return The canonical collection of LR(0) items.
     * @throws IllegalArgumentException If the grammar is not preprocessed or extended (= argument grammar).
     */
    public List<List<Item>> elements() {
        if(!isPreproc()) throw new IllegalArgumentException(Config.NONREC_PARSER_ERROR_PREPROCESSED);
        if(!isExtended()) throw new IllegalArgumentException(Config.NONREC_PARSER_ERROR_EXTENDED);

        final List<List<Item>> C = new ArrayList<>();
        final List<Item> I0 = closure(List.of(new Item(productions.get(0), 0)));

        C.add(I0);

        final List<Token> grammarSymbols = new ArrayList<>();
        grammarSymbols.addAll(vocabulary);
        grammarSymbols.addAll(alphabet);

        boolean changed = true;
        while(changed) {
            final List<List<Item>> C$ = new ArrayList<>();

            for(List<Item> I : C) {
                for(Token X : grammarSymbols) {
                    final List<Item> J = goTo(I, X);

                    if(J.size() > 0 && !C.contains(J) && !X.tokenType().name().equals(Config.LEXER_EMPTY_SYMBOL)) {
                        C$.add(J);
                    }
                }
            }

            if(C$.size() > 0) C.addAll(C$);
            else changed = false;
        }

        return C;
    }

    /**
     * Adds an argument production to the grammar.
     * Sets the extended flag to true.
     * @param token The new lhs token of the argument.
     */
    public void addArgumentProduction(final Token lhs) {
        if(extended) return;

        final List<Token[]> rhs = new ArrayList<>();
        rhs.add(new Token[] {
            startsymbol
        });

        vocabulary.add(lhs);
        productions.add(0, new Production(lhs, rhs));

        startsymbol = lhs;
        extended = true;
    }

    
    /**
     * Splits the given productions into a list of productions, where the rhs has only one symbol.
     * @param productions The productions which should be splitted.
     * @return The splitted productions.
     */
    private List<Production> splitProductions(final List<Production> productions) {
        final List<Production> splittedProductions = new ArrayList<>();

        productions.forEach(p -> {
            if(p.getRhs().size() > 1) {
                for(final Token[] rh : p.getRhs()) {
                    final List<Token[]> rhs = new ArrayList<>();
                    rhs.add(rh);

                    splittedProductions.add(new Production(p.getLhs(), rhs));
                }
            } else splittedProductions.add(p);
        });

        return splittedProductions;
    }


    /**
     * A Item is a production with a position in the rhs.
     */
    public static class Item {
        private final Production production;
        private final int position; // The dot position in the rhs: Start at 0 and end at length - 1.


        /**
         * Creates a new item.
         * @param production The production.
         * @param position The dot position in the rhs.
         */
        public Item(final Production production, final int position) {
            this.production = production;
            this.position = position;
        }


        @Override public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;

            final Item item = (Item) o;

            return position == item.position && production.equals(item.production);
        }

        @Override public String toString() {
            String rhs = "";
            for(int i = 0; i < production.getRhs().get(0).length; i++) {
                if(i == position) rhs += ".";
                rhs += production.getRhs().get(0)[i].symbol();
            }
            if(position == production.getRhs().get(0).length) rhs += ".";

            return production.getLhs().symbol() + " -> " + rhs;
        }


        public Production getProduction() {
            return production;
        }

        public int getPosition() {
            return position;
        }
    }
}