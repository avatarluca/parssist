package parssist.parser.bottom_up_analysis.lrparser.lr1parser.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import parssist.Config;
import parssist.lexer.Lexer;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.ParserGenerator;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.LRParser;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.LRParser.Action;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.LRParser.LRParseTable;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.SLRParser;
import parssist.parser.top_down_analysis.nrdparser.generator.exception.ParseException;
import parssist.parser.util.Grammar;
import parssist.parser.util.Production;
import parssist.parser.util.Grammar.Item;


/**
 * Class, which generates a SLR parser.
 */
public class SLRGenerator extends ParserGenerator {
    private final Grammar grammar;


    /**
     * Create a new SLR parser.
     * @param grammar Grammar.
     * @throws NullPointerException If the grammar is null.
     */
    public SLRGenerator(final Grammar grammar) throws NullPointerException {
        Objects.requireNonNull(grammar);   

        this.grammar = grammar;
    }


    @Override public String generate(String parserName, String packageName) throws Exception {
        LRParser slrParser = null;
        try {
            slrParser = new SLRParser(grammar);
        } catch (Exception e) {
            throw new ParseException(e.getMessage());
        }

        if(slrParser == null) return "";

        String parserCode = loadTemplate();
        final LRParseTable parseTable = slrParser.createParseTable();

        parserCode = insertCode(parserCode, generateParseTableCode(parseTable), Config.NONREC_PARSER_TEMPLATE_INIT_PARSETABLE);
        parserCode = insertCode(parserCode, generateTokentypesCode(), Config.NONREC_PARSER_TEMPLATE_INIT_TOKENTYPES);
        parserCode = insertCode(parserCode, generateAlphabetCode(), Config.NONREC_PARSER_TEMPLATE_INIT_ALPHABET);
        parserCode = insertCode(parserCode, generateVocabularyCode(), Config.NONREC_PARSER_TEMPLATE_INIT_VOCABULARY);
        parserCode = insertCode(parserCode, parserName, Config.NONREC_PARSER_TEMPLATE_CLASSNAME);
        parserCode = insertCode(parserCode, packageName.isEmpty() ? packageName : "package " + packageName, Config.NONREC_PARSER_TEMPLATE_PACKAGENAME);
        parserCode = insertCode(parserCode, grammar.getStartsymbol().tokenType().name(), Config.NONREC_PARSER_TEMPLATE_STARTSYMBOLNAME);
        parserCode = insertCode(parserCode, decorateRegex(grammar.getStartsymbol().tokenType().regex()), Config.NONREC_PARSER_TEMPLATE_STARTSYMBOLREGEX);
        parserCode = insertCode(parserCode, grammar.getStartsymbol().symbol(), Config.NONREC_PARSER_TEMPLATE_STARTSYMBOLVALUE);
    
        return parserCode;
    }


    /**
     * Generates the code for the parsetable.
     * Code is embedded in the parser.
     * A alternative would be to serialize the parse table and load it at runtime.
     * @param parseTable Parse table.
     * @return Code for the parse table.
     */
    private String generateParseTableCode(final LRParseTable parseTable) {
        final StringBuilder code = new StringBuilder();

        final Action[][] actions = parseTable.getActionTable();
        final int[][] gotos = parseTable.getGotoTable();
        final List<List<Item>> states = parseTable.getStates();
        
        code.append("final Action[][] actionTable = new Action[")
            .append(actions.length)
            .append("][")
            .append(actions[0].length)
            .append("];\n");
        
        for(int i = 0; i < actions.length; i++) {
            for(int j = 0; j < actions[i].length; j++) {
                if(actions[i][j] != null) {
                    code.append("actionTable[")
                        .append(i)
                        .append("][")
                        .append(j)
                        .append("] = new Action(Action.Type.")
                        .append(actions[i][j].type.name())
                        .append(", ")
                        .append(actions[i][j].value)
                        .append(");\n");
                }
            }
        }

        code.append("final int[][] gotoTable = new int[")
            .append(gotos.length)
            .append("][")
            .append(gotos[0].length)
            .append("];\n");
        
        for(int i = 0; i < gotos.length; i++) {
            for(int j = 0; j < gotos[i].length; j++) {
                code.append("gotoTable[")
                    .append(i)
                    .append("][")
                    .append(j)
                    .append("] = ")
                    .append(gotos[i][j])
                    .append(";\n");
            }
        }

        code.append("final List<List<Item>> states = new ArrayList<>();\n");

        for(int i = 0; i < states.size(); i++) {
            code.append("states.add(new ArrayList<>());\n");
            for(int j = 0; j < states.get(i).size(); j++) {
                code.append("states.get(")
                    .append(i)
                    .append(").add(new Item(new Production(new Token(new TokenType(\"")
                    .append(states.get(i).get(j).getProduction().getLhs().tokenType().name())
                    .append("\", \"")
                    .append(decorateRegex(states.get(i).get(j).getProduction().getLhs().tokenType().regex()))
                    .append("\", ")
                    .append(states.get(i).get(j).getProduction().getLhs().tokenType().priority())
                    .append(", ")
                    .append(states.get(i).get(j).getProduction().getLhs().tokenType().ignore())
                    .append("), \"")
                    .append(states.get(i).get(j).getProduction().getLhs().symbol())
                    .append("\"), new Token[][] {");

                for(int k = 0; k < states.get(i).get(j).getProduction().getRhs().size(); k++) {
                    code.append("{");
                    for(int l = 0; l < states.get(i).get(j).getProduction().getRhs().get(k).length; l++) {
                        code.append("new Token(new TokenType(\"")
                            .append(states.get(i).get(j).getProduction().getRhs().get(k)[l].tokenType().name())
                            .append("\", \"")
                            .append(decorateRegex(states.get(i).get(j).getProduction().getRhs().get(k)[l].tokenType().regex()))
                            .append("\", ")
                            .append(states.get(i).get(j).getProduction().getRhs().get(k)[l].tokenType().priority())
                            .append(", ")
                            .append(states.get(i).get(j).getProduction().getRhs().get(k)[l].tokenType().ignore())
                            .append("), \"")
                            .append(states.get(i).get(j).getProduction().getRhs().get(k)[l].symbol())
                            .append("\"), ");
                    }
                    code.append("}, ");
                }

                code.append("}), ")
                    .append(states.get(i).get(j).getPosition())
                    .append("));\n");
            }
        }

        code.append("parseTable = new LRParseTable(actionTable, gotoTable, states, alphabet, vocabulary);\n");
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
     * Loads the template txt file of a slr parser.
     * Because of webassembly, the template is hardcoded in the config.
     * @return The template.
     */
    private String loadTemplate() {
        return Config.SLR_TEMPLATE;
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
