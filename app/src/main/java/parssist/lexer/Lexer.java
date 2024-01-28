package parssist.lexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parssist.ParssistConfig;
import parssist.lexer.exception.InvalidLexFormatException;
import parssist.lexer.exception.InvalidTokenException;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.util.Reader;


/**
 * The lexer class, which tokenizes an input string.
 */
public class Lexer {
    private static final ParssistConfig CONFIG = ParssistConfig.getInstance();

    private final Reader reader = new Reader();

    private List<TokenType> tokentypes;
    private String code = "";


    /**
     * Creates a new Lexer.
     * @throws IOException If the file couldn't be read.
     * @throws InvalidLexFormatException If the file has invalid syntax.
     */
    public Lexer() throws IOException, InvalidLexFormatException {
        init();
    }


    public List<TokenType> getTokenTypes() {
        return tokentypes;
    }

    /**
     * Set via dependency injection the code which can get tokenized by {@link Lexer#tokenize()}.
     * @param code The code which can get tokenized.
     */
    public void setCode(final String code) {
        if(code != null) this.code = code;
    }


    /**
     * Tokenize a input string.
     * @throws InvalidTokenException When an invalid token is found.
     */
    public List<Token> tokenize() throws InvalidTokenException {
        final List<Token> tokens = new ArrayList<>();

        int cursor = 0;
        while(!code.isEmpty()) {
            final Token token = match();

            if(token == null) throw new InvalidTokenException(CONFIG.getProperty("LEXER.ERROR.CURSOR") + cursor);

            tokens.add(token);
            cursor += token.value().length();
        }

        return tokens;
    }


    /**
     * Initalizes the lexer by reading the lex file (alphabet), which is defined in {@link ParssistConfig}.
     * @throws IOException If the file couldn't be read.
     * @throws InvalidLexFormatException If the file has invalid syntax.
     */
    private final void init() throws IOException, InvalidLexFormatException {
        parseTokens(readLex());
    }

    /**
     * Reads the lex file.
     * @return The filecontent.
     * @throws IOException If the file couldn't be read.
     */
    private String readLex() throws IOException {
        return reader.read(CONFIG.getProperty("LEXER.INIT.INPUT.DIR"));
    }

    /**
     * Handcoded parser. In the future this could also be done by inventing a grammar and put it in the created parsergenerator.
     * Parses the tokens: [TOKENNAME] := "[REGEX]" and puts all tokens in {@link Lexer#tokentypes}
     * @param input The input string.
     * @throws InvalidLexFormatException If the file has invalid syntax.
     */
    private void parseTokens(final String input) throws InvalidLexFormatException {
        final Pattern ignorePattern = Pattern.compile(CONFIG.getProperty("LEXER.INIT.INPUT.IGNORE"));
        final Pattern commentPattern = Pattern.compile(CONFIG.getProperty("LEXER.INIT.INPUT.COMMENT"));
        final Pattern tokenPattern = Pattern.compile(CONFIG.getProperty("LEXER.INIT.INPUT.TOKENMAP"));
        final String[] rows = input.split(CONFIG.getProperty("LEXER.INIT.INPUT.ROWSPLIT"));
        final List<String> ignorables = new ArrayList<>();

        this.tokentypes = new ArrayList<>();

        int priority = 0;

        for(String row : rows) {
            final Matcher ignoreMatcher = ignorePattern.matcher(row);
            if(ignoreMatcher.find()) {
                row = row.substring(1, row.length());

                ignorables.add(row);

                continue;
            }

            final Matcher commentMatcher = commentPattern.matcher(row);
            if(commentMatcher.find()) continue;

            final Matcher tokenMatcher = tokenPattern.matcher(row);
            if(tokenMatcher.find()) {
                final String tokenName = tokenMatcher.group(1);
                final String tokenRegex = tokenMatcher.group(3);

                tokentypes.add(new TokenType(tokenName, tokenRegex, priority, false));
            } else {
                throw new InvalidLexFormatException(row);
            }

            priority++;
        }

        for(String ignorable : ignorables) {
            final String[] list = ignorable.split(",\\s*");

            for (String regex : list) {
                tokentypes.add(new TokenType(CONFIG.getProperty("LEXER.INIT.INPUT.IGNORE.TOKENNAME"), regex.replace("\"", ""), priority, true));
            }
        }
    }

    /**
     * Matches {@link code}.
     * @return The next token from {@link code}.
     */
    private Token match() {
        for(final TokenType tokenType : tokentypes) {
            final Pattern pattern = Pattern.compile(CONFIG.getProperty("LEXER.REGEX.STARTSYMBOL") + tokenType.regex());
            final Matcher matcher = pattern.matcher(this.code);

            if(matcher.find()) {
                final String value = matcher.group();
                this.code = this.code.substring(value.length());
                return new Token(tokenType, value);
            }
        }

        return null;
    }
}