package parssist;

import java.io.IOException;
import java.util.List;

import parssist.lexer.Lexer;
import parssist.lexer.exception.InvalidLexFormatException;
import parssist.lexer.exception.InvalidTokenException;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.util.Reader;
import parssist.util.Writer;

public class App {
    private static final ParssistConfig CONFIG = ParssistConfig.getInstance();


    public static void main(String[] args) throws IOException, InvalidLexFormatException, InvalidTokenException {
        final Reader reader = new Reader();
        final Writer writer = new Writer();
        final Lexer lexer = new Lexer();

        final String code = reader.read(CONFIG.getProperty("PARSER.INIT.INPUT.DIR"));
        lexer.setCode(code);
        List<Token> tokens = lexer.tokenize();
        List<TokenType> tokenTypes = lexer.getTokenTypes();

        writer.write(CONFIG.getProperty("LEXER.TOKENTABLE.OUTPUT.DIR"), tokenTypes, tokens);
    }
}
