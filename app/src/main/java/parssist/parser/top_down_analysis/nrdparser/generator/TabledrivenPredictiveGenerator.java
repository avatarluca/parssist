package parssist.parser.top_down_analysis.nrdparser.generator;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Table;

import parssist.parser.ParserGenerator;
import parssist.parser.top_down_analysis.nrdparser.parser.TabledrivenPredictiveParser;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Grammar;
import parssist.parser.top_down_analysis.nrdparser.parser.util.Production;


/**
 * Class, which generates a table-driven predictive parser code.
 */
public class TabledrivenPredictiveGenerator extends ParserGenerator{
    private final Grammar grammar;


    /**
     * Constructor.
     * @param grammar Grammar.
     * @throws NullPointerException If the grammar is null.
     */
    public TabledrivenPredictiveGenerator(final Grammar grammar) throws NullPointerException {
        Objects.requireNonNull(grammar);   

        this.grammar = grammar;
    }


    @Override public String generateParser(final String parserName, final String packageName) {
        final StringBuilder parserCode = new StringBuilder();
        final TabledrivenPredictiveParser parser = new TabledrivenPredictiveParser(grammar);

        List<Production>[][] parseTable = parser.getParseTable();
        final String parseTableCode = generateParseTableCode(parseTable);

        return parserCode.toString();
    }


    /**
     * Generate the code for the parse table.
     * @param parseTable Parse table.
     * @return Code for the parse table.
     */
    private String generateParseTableCode(final List<Production>[][] parseTable) {
        final StringBuilder parseTableCode = new StringBuilder();

        parseTableCode.append("private static final List<Production>[][] PARSE_TABLE = new List<Production>[][] {");

        for(int i = 0; i < parseTable.length; i++) {
            parseTableCode.append("{");

            for(int j = 0; j < parseTable[i].length; j++) {
                parseTableCode.append("new ArrayList<Production>()");

                if(j < parseTable[i].length - 1) parseTableCode.append(", ");
            }

            parseTableCode.append("}");

            if(i < parseTable.length - 1) parseTableCode.append(", ");
        }

        parseTableCode.append("};");

        return parseTableCode.toString();
    }

    
}
