package parssist;

import java.util.List;

import javax.management.RuntimeErrorException;

import parssist.lexer.Lexer;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.generator.SLRGenerator;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.LRParser;
import parssist.parser.bottom_up_analysis.lrparser.lr1parser.parser.SLRParser;
import parssist.parser.top_down_analysis.nrdparser.generator.GrammarGenerator;
import parssist.parser.top_down_analysis.nrdparser.generator.TabledrivenPredictiveGenerator;
import parssist.parser.top_down_analysis.nrdparser.parser.TabledrivenPredictiveParser;
import parssist.parser.top_down_analysis.nrdparser.parser.exception.NoLL1GrammarException;
import parssist.util.Reader;


/**
 * The main class of the application.
 * Because of webassembly, a lot of try catch blocks are used.
 */
public class App {
    /**
     * The main method of the application. 
     * It handles the command line arguments.
     * @param args The command line arguments.
     * @throws Exception If there was any further exception.
     */
    public static void main(String... args) throws Exception {
        if(args.length > 0) {
            final String command = args[0];
            
            switch(command) {
                case "codegeneration":
                    String code_lex = "", code_grammar = "", code_name = "", code_module = "", code_algorithm = "auto", mode = "0";

                    if(args.length > 1) code_lex = args[1];
                    if(args.length > 2) code_grammar = args[2];
                    if(args.length > 3) code_name = args[3];
                    if(args.length > 4) code_module = args[4];
                    if(args.length > 5) code_algorithm = args[5];
                    if(args.length > 6) mode = args[6];

                    if(mode.equals("1")) {
                        // code_grammar and code_lex are file paths
                        handleCodeGenerationFile(code_lex, code_grammar, code_name, code_module, code_algorithm);
                    } else {
                        handleCodeGeneration(code_lex, code_grammar, code_name, code_module, code_algorithm);
                    }
                    break;
                case "parsetree":
                    String parsetree_lex = "", parsetree_grammar = "", parsetree_input = "", parsetree_algorithm = "auto";

                    if(args.length > 1) parsetree_lex = args[1];
                    if(args.length > 2) parsetree_grammar = args[2];
                    if(args.length > 3) parsetree_input = args[3];
                    if(args.length > 4) parsetree_algorithm = args[4];

                    handleParseTree(parsetree_lex, parsetree_grammar, parsetree_input, parsetree_algorithm);
                    break;
                case "tokentable":
                    String tokentable_lex = "", tokentable_input = "";

                    if(args.length > 1) tokentable_lex = args[1];
                    if(args.length > 2) tokentable_input = args[2];

                    handleTokenTable(tokentable_lex, tokentable_input);
                    break;
                case "validate": 
                    String val_lex = "", val_grammar = "", val_input = "", val_algorithm = "auto";

                    if(args.length > 1) val_lex = args[1];
                    if(args.length > 2) val_grammar = args[2];
                    if(args.length > 3) val_input = args[3];
                    if(args.length > 4) val_algorithm = args[4];

                    handleValidate(val_lex, val_grammar, val_input, val_algorithm);
                default:
                    break;
            }
        }
    }

    /**
     * Handle the code generation.
     * @param lex The lex file content.
     * @param grammar The grammar file content.
     * @param name The name of the generated parser.
     * @param module The module of the generated parser.
     * @param algorithm The algorithm of the generated parser.
     */
    private static void handleCodeGeneration(final String lex, final String grammar, final String name, final String module, final String algorithm) {
        try {
            switch(algorithm) {
                case "ll1":
                    handleLL1(lex, grammar, name, module);                   
                    break;
                case "slr1":
                    handleSLR(lex, grammar, name, module);
                    break;
                case "auto":
                default:
                    boolean isLL1 = false;

                    try {
                        isLL1 = handleLL1(lex, grammar, name, module);
                    } catch(Exception e) {
                        isLL1 = false;
                    }

                    if(!isLL1) {
                        final boolean isSLR = handleSLR(lex, grammar, name, module);

                        if(!isSLR) {
                            System.out.println("Further algorithms are not implemented yet.");
                        }
                    }
                    break;
            }
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Handle the code generation with file.
     * @param lex The lex file path.
     * @param grammar The grammar file path.
     * @param name The name of the generated parser.
     * @param module The module of the generated parser.
     * @param algorithm The algorithm of the generated parser.
     */
    private static void handleCodeGenerationFile(final String lex, final String grammar, final String name, final String module, final String algorithm) {
        try {
            final Reader reader = new Reader();
            final String lexContent = reader.read(lex);
            final String grammarContent = reader.read(grammar);

            handleCodeGeneration(lexContent, grammarContent, name, module, algorithm);
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Handle the parse tree.
     * @param lex The lex file content.
     * @param grammar The grammar file content.
     * @param input The input string.
     * @param algorithm The algorithm of the generated parser.
     */
    private static void handleParseTree(final String lex, final String grammar, final String input, final String algorithm) {
        try {
            switch(algorithm) {
                case "ll1":
                    handleLL1ParseTree(lex, grammar, input);           
                    break;
                case "slr1":
                    handleSLRParseTree(lex, grammar, input);
                    break;
                case "auto":    
                default:
                    boolean isLL1 = false;

                    try {
                        isLL1 = handleLL1ParseTree(lex, grammar, input);
                    } catch(Exception e) {
                        isLL1 = false;
                    }

                    if(!isLL1) {
                        final boolean isSLR = handleSLRParseTree(lex, grammar, input);
                        
                        if(!isSLR) {
                            System.out.println("Further algorithms are not implemented yet.");
                        } 
                    }
                    
                    break;
            }
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Handle the tokentable.
     * It prints the tokentypes and tokens in a JSON like format, to parse it in javascript.
     * @param lex The lex file content.
     * @param input The input string.
     */
    private static void handleTokenTable(final String lex, final String input) {
        try {
            final Lexer lexer = new Lexer(lex);
            lexer.setCode(input);
            lexer.parseTokens(lex);
            
            List<TokenType> tokentypes = lexer.getTokenTypes();
            List<Token> tokens = lexer.tokenize();

            // JSON like output
            String tokentypesJson = "{";
            String tokensJson = "{";

            for(int i = 0; i < tokentypes.size(); i++) {
                final TokenType currentTokenType = tokentypes.get(i);
                tokentypesJson += "\""+ i + "\": {\"type\": \"" + currentTokenType.name() + "\", \"regex\": \"" + currentTokenType.regex().replace("\\", "\\\\") + "\", \"ignore\": \"" + currentTokenType.ignore() + "\", \"priority\": \"" + currentTokenType.priority() + "\"}";
                if(i < tokentypes.size() - 1) tokentypesJson += ", ";
            }
            tokentypesJson += "}";

            for(int i = 0; i < tokens.size(); i++) {
                final Token currentToken = tokens.get(i);
                tokensJson += "\""+ i + "\": {\"type\": \"" + currentToken.tokenType().name() + "\", \"symbol\": \"" + currentToken.symbol().replace("\\", "\\\\") + "\"}";
                if(i < tokens.size() - 1) tokensJson += ", ";
            }
            tokensJson += "}";
            
            System.out.println("{\"tokentypes\": " + tokentypesJson + ", \"tokens\": " + tokensJson + "}"); 
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    /**
     * Handle the validation of the grammar.
     * @param lex The lex file content.
     * @param grammar The grammar file content.
     * @param input The input string.
     * @param algorithm The algorithm of the generated parser.
     */
    private static void handleValidate(final String lex, final String grammar, final String input, final String algorithm) {
        boolean isValid = false;
        try {
            switch(algorithm) {
                case "ll1":
                    isValid = handleLL1Validation(lex, grammar, input);                   
                    break;
                case "slr1":
                    isValid = handleSLRValidation(lex, grammar, input);
                    break;
                case "auto":    
                default:
                    boolean isLL1 = false;

                    try {
                        isLL1 = handleLL1Validation(lex, grammar, input);
                    } catch(Exception e) {
                        isLL1 = false;
                    }

                    if(!isLL1) {
                        final boolean isSLR = handleSLRValidation(lex, grammar, input);

                        if(!isSLR) {
                            System.out.println("Further algorithms are not implemented yet.");
                        } else isValid = true;
                    } else isValid = true;
                    
                    break;
            }
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // because of webassembly and bypass print stream, we need to print the result here
        if(isValid) System.out.println("The input is valid.");
        else System.out.println("The input is not valid.");
    }

    /**
     * Handle the LL1 algorithm.
     * @param lex The lex file content.
     * @param grammar The grammar file content.
     * @param name The name of the generated parser.
     * @param module The module of the generated parser.
     * @return True if the grammar is LL(1), false otherwise.
     * @throws Exception If an exception occurs.
     */
    private static boolean handleLL1(String lex, String grammar, String name, String module) throws Exception {
        try {
            final Lexer lexer = new Lexer(lex);
            lexer.parseTokens(lex);
            
            final GrammarGenerator generator = new GrammarGenerator(grammar, lexer.getTokenTypes(), true);
            
            final TabledrivenPredictiveGenerator tabledrivenPredictiveGenerator = new TabledrivenPredictiveGenerator(generator.generate());
            System.out.println(tabledrivenPredictiveGenerator.generate(name, module)); 
        } catch(NoLL1GrammarException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        } catch(Exception e) {
            throw e;
        }

        return true;
    }

    /**
     * Handle the SLR algorithm.
     * @param lex The lex file content.
     * @param grammar The grammar file content.
     * @param name The name of the generated parser.
     * @param module The module of the generated parser.
     * @return True if the grammar is SLR(1), false otherwise.
     * @throws Exception If an exception occurs.
     */
    private static boolean handleSLR(String lex, String grammar, String name, String module) throws Exception {
        try {
            final Lexer lexer = new Lexer(lex);
            lexer.parseTokens(lex);
            
            final GrammarGenerator generator = new GrammarGenerator(grammar, lexer.getTokenTypes(), true);
            
            final SLRGenerator slrGenerator = new SLRGenerator(generator.generate());
            System.out.println(slrGenerator.generate(name, module)); 
        } catch(NoLL1GrammarException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        } catch(Exception e) {
            throw e;
        }

        return true;
    }

    /**
     * Handle the LL1 validation.
     * @param lex The lex file content.
     * @param grammar The grammar file content.
     * @param input The input string.
     * @return True if the grammar is LL(1), false otherwise.
     * @throws Exception If an exception occurs.
     */
    private static boolean handleLL1Validation(String lex, String grammar, String input) throws Exception {
        try {
            final Lexer lexer = new Lexer(lex);
            lexer.parseTokens(lex);
            
            final GrammarGenerator generator = new GrammarGenerator(grammar, lexer.getTokenTypes(), true);
            
            final TabledrivenPredictiveParser tabledrivenPredictiveParser = new TabledrivenPredictiveParser(generator.generate(), input);
            return tabledrivenPredictiveParser.computeSystemAnalysis();
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * Handle the SLR validation.
     * @param lex The lex file content.
     * @param grammar The grammar file content.
     * @param input The input string.
     * @return True if the grammar is SLR(1), false otherwise.
     * @throws Exception If an exception occurs.
     */
    private static boolean handleSLRValidation(String lex, String grammar, String input) throws Exception {
        try {
            final Lexer lexer = new Lexer(lex);
            lexer.parseTokens(lex);
            
            final GrammarGenerator generator = new GrammarGenerator(grammar, lexer.getTokenTypes(), true);

            final LRParser slrParser = new SLRParser(generator.generate(), input);
            slrParser.parse(input);
            return true;
        } catch(Exception e) {            
            return false;
        }
    }

    /**
     * Handle the LL1 parsetree.
     * @param lex The lex file content.
     * @param grammar The grammar file content.
     * @param input The input string.
     * @return True if the grammar is LL(1), false otherwise.
     * @throws Exception If an exception occurs.
     */
    private static boolean handleLL1ParseTree(String lex, String grammar, String input) throws Exception {
        try {
            final Lexer lexer = new Lexer(lex);
            lexer.parseTokens(lex);
            
            final GrammarGenerator generator = new GrammarGenerator(grammar, lexer.getTokenTypes(), true);

            final TabledrivenPredictiveParser tabledrivenPredictiveGenerator = new TabledrivenPredictiveParser(generator.generate(), input);
            tabledrivenPredictiveGenerator.computeSystemAnalysis();
            tabledrivenPredictiveGenerator.printParseTree();  
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * Handle the SLR parsetree.
     * @param lex The lex file content.
     * @param grammar The grammar file content.
     * @param input The input string.
     * @return True if the grammar is SLR(1), false otherwise.
     * @throws Exception If an exception occurs.
     */
    private static boolean handleSLRParseTree(String lex, String grammar, String input) throws Exception {
        try {
            final Lexer lexer = new Lexer(lex);
            lexer.parseTokens(lex);
            
            final GrammarGenerator generator = new GrammarGenerator(grammar, lexer.getTokenTypes(), true);

            final LRParser slrParser = new SLRParser(generator.generate(), input);
            slrParser.parse(input);
            slrParser.printParseTree();  
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}