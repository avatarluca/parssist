# Parser with recursive descent

## Implementation
Since the lexer was already created during the creation (but no generator is available yet), the alphabet of the input grammar was defined in a lex file. then the lexer is called which returns a list of tokens. This parser is the first one, so there is a handcoded parser that executes the lexer with the input grammar when called.