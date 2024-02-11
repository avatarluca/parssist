package parssist.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import parssist.lexer.util.Token;
import parssist.lexer.util.TokenType;
import parssist.Config;


/**
 * Writer class, used to write a file.
 */
public class Writer {
    /**
     * Write the tokentable file.
     * @param path The filepath.
     * @throws IOException If the file couldn't be read.
     */
    public void write(final String path, final List<TokenType> tokentypes, final List<Token> tokens) throws IOException {
        final File file = new File(path);

        if(!file.exists()) {
            file.createNewFile();
        }

        String content = "";
        
        content += Config.LEXER_TOKENTABLE_TITLE1;
        for(int i = 0; i < tokentypes.size(); i++) {
            final TokenType currentTokenType = tokentypes.get(i);
            content += i + " " + currentTokenType + "\n";
        }

        content += Config.LEXER_TOKENTABLE_TITLE2;
        for(int i = 0; i < tokens.size(); i++) {
            final Token currentToken = tokens.get(i);
            content += i + " " + currentToken + "\n";
        }
        
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
}
