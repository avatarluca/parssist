# Parssist - A parser assistant
A Parsergenerator, which takes an extended BNF and builds a Parser (with a certain algorithm).

# Get started
Basically, Parssist takes a grammar and a lexical definition described in the [wiki](https://github.com/avatarluca/parssist/wiki). After cloning or forking the project, the predefined files `input.lex` and `input.gra` in the input directory as well as own files can be edited and executed as input (as well as a few additional arguments described [here](#command-line-arguments)). These two methods are described in more detail below.

## Online Editor (recommended)
For using the [online editor](https://www.parsergenerator.valenzelektron.com/web/parssist.html) read the [wiki](https://github.com/avatarluca/parssist/wiki).

## Command line
There are 3 ways to use parssist in command line:
#### Existing grammar definition
1. Write your grammar in the input.gra file (inside of the input directory).
2. Write your lexer in the input.lex file (inside of the input directory).
3. Call ```./run.sh```.
#### Own grammar definition
1. Write a grammarfile.
2. Write a lexerfile.
3. Call ```./parssist -g [RELATIVE GRAMMAR FILE PATH] -l [RELATIVE LEXER FILE PATH]```.
#### Inline definition
1. Write grammar directly with the `-g` argument.
2. Write lexer directly with the `-l` argument.
3. Set mode to 0 (default 1 for path input) `-mode 0`

### Command line arguments
Argument       | Description
---------------|--------------------------------------------------------
-g / --grammar | relative file path to grammar file or inline grammar
-l / --lexer | relative file path to lexer file or inline lexer
-lang / --language | Parser programming language
-a / --algorithm | Parser algorithm
-n / --name | Parser name
-m / --module | Parser module name
-mode / --mode | Input mode
-pa / --print-algorithms | Print the available parser algorithms and keywords for the algorithm argument
-pl / --print-languages | Print the available parser programming languages and keywords for the language argument

# Todo
- LR-Parser
- Error Recovery Methods (Settings)
- Add semantic predicates
- Other Languages
- Handling Spaces and other cases in input
