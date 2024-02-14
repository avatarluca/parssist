package parssist.parser.top_down_analysis.nrdparser.generator;

import java.util.List;
import java.util.Objects;
import java.util.regex.PatternSyntaxException;

import parssist.Config;
import parssist.parser.ParserGenerator;
import parssist.parser.top_down_analysis.nrdparser.generator.exception.GenerationException;
import parssist.parser.top_down_analysis.nrdparser.parser.TabledrivenPredictiveParser;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NoLL1GrammarException;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Grammar;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Production;


/**
 * Class, which generates a table-driven predictive parser code.
 */
public final class TabledrivenPredictiveGenerator extends ParserGenerator {
    private final Grammar grammar;


    /**
     * Create a new table-driven predictive parser generator.
     * @param grammar Grammar.
     * @throws NullPointerException If the grammar is null.
     */
    public TabledrivenPredictiveGenerator(final Grammar grammar) throws NullPointerException {
        Objects.requireNonNull(grammar);   

        this.grammar = grammar;
    }


    @Override public String generate(final String parserName, final String packageName) throws NoLL1GrammarException, IndexOutOfBoundsException, GenerationException {
        TabledrivenPredictiveParser parser = null;
        try {
            parser = new TabledrivenPredictiveParser(grammar);
        } catch(StackOverflowError | PatternSyntaxException e) {
            throw new GenerationException(e.getMessage());
        }

        if(parser == null) return "";

        String parserCode = loadTemplate();
        final List<Production>[][] parseTable = parser.getParseTable();

        if(!parser.isLL1(parseTable)) throw new NoLL1GrammarException(Config.NONREC_PARSER_ERROR_NO_LL1_GRAMMAR);

        parserCode = insertCode(parserCode, generateParseTableCode(parseTable), Config.NONREC_PARSER_TEMPLATE_INIT_PARSETABLE);
        parserCode = insertCode(parserCode, generateTokentypesCode(), Config.NONREC_PARSER_TEMPLATE_INIT_TOKENTYPES);
        parserCode = insertCode(parserCode, generateAlphabetCode(), Config.NONREC_PARSER_TEMPLATE_INIT_ALPHABET);
        parserCode = insertCode(parserCode, generateVocabularyCode(), Config.NONREC_PARSER_TEMPLATE_INIT_VOCABULARY);
        parserCode = insertCode(parserCode, parserName, Config.NONREC_PARSER_TEMPLATE_CLASSNAME);
        parserCode = insertCode(parserCode, packageName.isEmpty() ? packageName : "package " + packageName, Config.NONREC_PARSER_TEMPLATE_PACKAGENAME);
        parserCode = insertCode(parserCode, "" + parseTable.length, Config.NONREC_PARSER_TEMPLATE_PARSETABLESIZEY);
        parserCode = insertCode(parserCode, "" + parseTable[0].length, Config.NONREC_PARSER_TEMPLATE_PARSETABLESIZEX);
        parserCode = insertCode(parserCode, grammar.getStartsymbol().tokenType().name(), Config.NONREC_PARSER_TEMPLATE_STARTSYMBOLNAME);
        parserCode = insertCode(parserCode, decorateRegex(grammar.getStartsymbol().tokenType().regex()), Config.NONREC_PARSER_TEMPLATE_STARTSYMBOLREGEX);
        parserCode = insertCode(parserCode, grammar.getStartsymbol().symbol(), Config.NONREC_PARSER_TEMPLATE_STARTSYMBOLVALUE);

        return parserCode.toString();
    }


    /**
     * Generates the code for the parsetable.
     * Code is embedded in the parser.
     * A alternative would be to serialize the parse table and load it at runtime.
     * @param parseTable Parse table.
     * @return Code for the parse table.
     */
    private String generateParseTableCode(final List<Production>[][] parseTable) {
        final StringBuilder code = new StringBuilder();

        int listnr = 0;

        for(int i = 0; i < parseTable.length; i++) {
            for(int j = 0; j < parseTable[i].length; j++) {
                if(parseTable[i][j] != null) {
                    code.append("parseTable[")
                        .append(i)
                        .append("][")
                        .append(j)
                        .append("] = new ArrayList<>();\n");

                    for(Production p : parseTable[i][j]) {
                        code.append("List<Token[]> list")
                            .append(listnr)
                            .append(" = new ArrayList<>();\n")
                            .append("list")
                            .append(listnr)
                            .append(".add(new Token[]{");

                        for(int k = 0; k < p.getRhs().get(0).length; k++) {
                            code.append("new Token(new TokenType(\"")
                                .append(p.getRhs().get(0)[k].tokenType().name())
                                .append("\", \"")
                                .append(decorateRegex(p.getRhs().get(0)[k].tokenType().regex()))
                                .append("\", ")
                                .append(p.getRhs().get(0)[k].tokenType().priority())
                                .append(", ")
                                .append(p.getRhs().get(0)[k].tokenType().ignore())
                                .append("), \"")
                                .append(p.getRhs().get(0)[k].symbol())
                                .append("\")");
                                
                            if(k < p.getRhs().get(0).length - 1) code.append(", ");
                        }

                        code.append("});\n");

                        code.append("parseTable[")
                            .append(i)
                            .append("][")
                            .append(j)
                            .append("].add(new Production(new Token(new TokenType(\"")
                            .append(p.getLhs().tokenType().name())
                            .append("\", \"")
                            .append(decorateRegex(p.getLhs().tokenType().regex()))
                            .append("\", ")
                            .append(p.getLhs().tokenType().priority())
                            .append(", ")
                            .append(p.getLhs().tokenType().ignore())
                            .append("), \"")
                            .append(p.getLhs().symbol())
                            .append("\"), list")
                            .append(listnr++);
                        code.append("));\n");
                    }
                }
            }
        }

        return code.toString();
    }

    /**
     * Generates the code for the tokentypes.
     * @return Code for the tokentypes.
     */
    private String generateTokentypesCode() {
        final StringBuilder code = new StringBuilder();

        for(int i = 0; i < grammar.getTokentypes().size(); i++) {
            code.append("tokentypes.add(new TokenType(\"")
                .append(grammar.getTokentypes().get(i).name())
                .append("\", \"")
                .append(decorateRegex(grammar.getTokentypes().get(i).regex()))
                .append("\", ")
                .append(grammar.getTokentypes().get(i).priority())
                .append(", ")
                .append(grammar.getTokentypes().get(i).ignore())
                .append("));\n");
        }

        return code.toString();
    }

    /**
     * Generates the code for the alphabet.
     * @return Code for the alphabet.
     */
    private String generateAlphabetCode() {
        final StringBuilder code = new StringBuilder();

        for(int i = 0; i < grammar.getAlphabet().size(); i++) {
            code.append("alphabet.add(new Token(new TokenType(\"")
                .append(grammar.getAlphabet().get(i).tokenType().name())
                .append("\", \"")
                .append(decorateRegex(grammar.getAlphabet().get(i).tokenType().regex()))
                .append("\", ")
                .append(grammar.getAlphabet().get(i).tokenType().priority())
                .append(", ")
                .append(grammar.getAlphabet().get(i).tokenType().ignore())
                .append("), \"")
                .append(grammar.getAlphabet().get(i).symbol())
                .append("\"));\n");
        }

        return code.toString();
    }

    /**
     * Generates the code for the vocabulary.
     * @return Code for the vocabulary.
     */
    private String generateVocabularyCode() {
        final StringBuilder code = new StringBuilder();

        for(int i = 0; i < grammar.getVocabulary().size(); i++) {
            code.append("vocabulary.add(new Token(new TokenType(\"")
                .append(grammar.getVocabulary().get(i).tokenType().name())
                .append("\", \"")
                .append(decorateRegex(grammar.getVocabulary().get(i).tokenType().regex()))
                .append("\", ")
                .append(grammar.getVocabulary().get(i).tokenType().priority())
                .append(", ")
                .append(grammar.getVocabulary().get(i).tokenType().ignore())
                .append("), \"")
                .append(grammar.getVocabulary().get(i).symbol())
                .append("\"));\n");
        }

        return code.toString();
    }

    /**
     * Loads the template txt file of a nrd parser.
     * Because of webassembly, the template is hardcoded in the config.
     * @return The template.
     */
    private String loadTemplate() {
        return Config.TEMPLATE;
    }

    /**
     * Replaces the token in the template with the code.
     * @param template The template.
     * @param code The code, which gets inserted.
     * @param token The token, which gets replaced.
     * @return The edited template.
     */
    private String insertCode(final String template, final String code, final String token) {
        return template.replace(Config.NONREC_PARSER_TEMPLATE_OPENTOKEN + token + Config.NONREC_PARSER_TEMPLATE_CLOSETOKEN, code);
    }

    /**
     * Decorate regex with double backslashes.
     * @param regex The regex.
     * @return The decorated regex.
     */
    private String decorateRegex(final String regex) {
        return regex.replace("\\", "\\\\");
    }
}