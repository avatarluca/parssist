package parssist.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * Reader class, used to read the input file.
 */
public class Reader {
    /**
     * Read the input file.
     * @param path The filepath.
     * @throws IOException If the file couldn't be read.
     */
    public String read(final String path) throws IOException {
        final File file = new File(path);
        
        try(final FileReader reader = new FileReader(file)) {
            String code = "";  
            int character;

            while((character = reader.read()) != -1) {
                code += (char) character;
            }

            return code;
        }
    }
}