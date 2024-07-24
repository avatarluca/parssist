package parssist.lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parssist.Config;
import parssist.lexer.exception.InvalidLexFormatException;
import parssist.lexer.exception.InvalidTokenException;
import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;


/**
 * The lexer class, which tokenizes an input string.
 */
public class Lexer {
    private List<TokenType> tokentypes;
    private String code = "";


    /**
     * Creates a new Lexer.
     * Because of webassembly, the lexer can't read files. The lex file content has to be passed as a string.
     * @param lex The lex file content.
     * @throws IOException If the file couldn't be read.
     * @throws InvalidLexFormatException If the file has invalid syntax.
     */
    public Lexer(final String lex) throws IOException, InvalidLexFormatException {
        init(lex);
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
     * Tokenize a input string (give a list of terminals from the input string).
     * @throws InvalidTokenException When an invalid token is found.
     */
    public List<Token> tokenize() throws InvalidTokenException {
        final List<Token> tokens = new ArrayList<>();

        int cursor = 0;

        while(!code.isEmpty()) {
            final Token token = match();

            if(token == null) throw new InvalidTokenException(Config.LEXER_ERROR_CURSOR + cursor);

            tokens.add(token);
            cursor += token.symbol().length();
        }

        return tokens;
    }

    /**
     * Handcoded parser. In the future this could also be done by inventing a grammar and put it in the created parsergenerator.
     * Parses the tokens: [TOKENNAME] := "[REGEX]" and puts all tokens in {@link Lexer#tokentypes}
     * @param input The input string.
     * @throws InvalidLexFormatException If the file has invalid syntax.
     */
    public void parseTokens(final String input) throws InvalidLexFormatException {
        final Pattern ignorePattern = Pattern.compile(Config.LEXER_INIT_INPUT_IGNORE);
        final Pattern commentPattern = Pattern.compile(Config.LEXER_INIT_INPUT_COMMENT);
        final Pattern tokenPattern = Pattern.compile(Config.LEXER_INIT_INPUT_TOKENMAP);
        final String[] rows = input.split(Config.LEXER_INIT_INPUT_ROWSPLIT);
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
                tokentypes.add(new TokenType(Config.LEXER_INIT_INPUT_IGNORE_TOKENNAME, regex.replace("\"", ""), 0, true));
            }
        }

        this.tokentypes.sort((a, b) -> a.priority() - b.priority());
    }


    /**
     * Initalizes the lexer by reading the lex file (alphabet), which is defined in {@link ParssistConfig}.
     * @param lex The lex file content.
     * @throws IOException If the file couldn't be read.
     * @throws InvalidLexFormatException If the file has invalid syntax.
     */
    private final void init(final String lex) throws IOException, InvalidLexFormatException {
        parseTokens(lex);
    }

    /**
     * Matches {@link code}.
     * @return The next token from {@link code}.
     */
    private Token match() {
        for(final TokenType tokenType : tokentypes) {
            final Pattern pattern = Pattern.compile(Config.LEXER_REGEX_STARTSYMBOL + tokenType.regex());
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