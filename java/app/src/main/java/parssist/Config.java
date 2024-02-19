package parssist;


/**
 * Public static configuration class.
 * This is needed because of webassembly limitations (no files are loadable).
 */
public class Config {
    public static final String LEXER_INIT_INPUT_IGNORE="^%( *\"(.*)\" *,)*( *\"(.*)\" *)";
    public static final String LEXER_INIT_INPUT_IGNORE_TOKENNAME="IGNORE";
    public static final String LEXER_INIT_INPUT_COMMENT="^#";
    public static final String LEXER_INIT_INPUT_TOKENMAP="^([a-zA-Z0-9_]*)( *:= *)\"(.*)\"";
    public static final String LEXER_INIT_INPUT_ROWSPLIT="\n";
    public static final String LEXER_ERROR_CURSOR="Invalid token at";
    public static final String LEXER_REGEX_STARTSYMBOL="^";
    public static final String LEXER_REGEX_ENDSYMBOL="$";
    public static final String LEXER_TOKENTABLE_TITLE1="\n======Tokentype lex list========================================================\n";
    public static final String LEXER_TOKENTABLE_TITLE2="\n======Tokens with ignorables====================================================\n";
    public static final String LEXER_EMPTY_SYMBOL="EMPTY_SYMBOL";
    public static final String LEXER_TERMINAL="TERMINAL";
    public static final String LEXER_NONTERMINAL="NONTERMINAL";


    public static final String GRAMMAR_TOKEN_VOCABULARY_ASSIGNMENT="VOCABULARY_ASSIGNMENT";
    public static final String GRAMMAR_TOKEN_ALPHABET_ASSIGNMENT="ALPHABET_ASSIGNMENT";
    public static final String GRAMMAR_TOKEN_STARTSYMBOL_ASSIGNMENT="STARTSYMBOL_ASSIGNMENT";
    public static final String GRAMMAR_TOKEN_SET="SET";
    public static final String GRAMMAR_TOKEN_NONTERMINAL="NONTERMINAL";
    public static final String GRAMMAR_TOKEN_PRODUCTION_SYMBOL="PRODUCTION_SYMBOL";
    public static final String GRAMMAR_TOKEN_PRODUCTION_OR="PRODUCTION_OR";
    public static final String GRAMMAR_TOKEN_PRODUCTION_RULE="PRODUCTION_RULE";
    public static final String GRAMMAR_TOKEN_EMPTY_SYMBOL="EMPTY_SYMBOL";
    public static final String GRAMMAR_TOKEN_ERROR_VOCABULARY_ASSIGNMENT="Illegal vocabulary assignment";
    public static final String GRAMMAR_TOKEN_ERROR_ALPHABET_ASSIGNMENT="Illegal alphabet assignment";
    public static final String GRAMMAR_TOKEN_ERROR_STARTSYMBOL_ASSIGNMENT="Illegal startsymbol assignment";
    public static final String GRAMMAR_TOKEN_ERROR_PRODUCTION_RULE="Illegal productionrule assignment";
    public static final String GRAMMAR_TOKEN_PRODUCTIONSYMBOLS_REGEX="^((\\-> *)|(\\| *))";
    public static final String GRAMMAR_ARGUMENT_SYMBOL = "S_";

    public static final String PARSER_INIT_INPUT_DIR="definition/grammar/input.gra";

    public static final String NONREC_PARSER_ERROR_EMPTY_SYMBOL="Empty Symbol";
    public static final String NONREC_PARSER_ERROR_INVALID_TOKEN="Invalid Token";
    public static final String NONREC_PARSER_ERROR_PREPROCESSED="Grammar should be preprocessed";
    public static final String NONREC_PARSER_ERROR_EXTENDED="Grammar should be extended";
    public static final String NONREC_PARSER_ERROR_NO_LL1_GRAMMAR="Grammar is no LL1";
    public static final String NONREC_PARSER_ERROR_NO_PRODUCTION="No next production";
    public static final String NONREC_PARSER_TEMPLATE_OPENTOKEN="{{";
    public static final String NONREC_PARSER_TEMPLATE_CLOSETOKEN="}}";
    public static final String NONREC_PARSER_TEMPLATE_INIT_PARSETABLE="init_parsetable";
    public static final String NONREC_PARSER_TEMPLATE_INIT_TOKENTYPES="init_tokentypes";
    public static final String NONREC_PARSER_TEMPLATE_INIT_ALPHABET="init_alphabet";
    public static final String NONREC_PARSER_TEMPLATE_INIT_VOCABULARY="init_vocabulary";
    public static final String NONREC_PARSER_TEMPLATE_CLASSNAME="parser_name";
    public static final String NONREC_PARSER_TEMPLATE_PACKAGENAME="package_name";
    public static final String NONREC_PARSER_TEMPLATE_PARSETABLESIZEY="parsetable_y_size";
    public static final String NONREC_PARSER_TEMPLATE_PARSETABLESIZEX="parsetable_x_size";
    public static final String NONREC_PARSER_TEMPLATE_STARTSYMBOLNAME="start_symbol_name";
    public static final String NONREC_PARSER_TEMPLATE_STARTSYMBOLREGEX="start_symbol_regex";
    public static final String NONREC_PARSER_TEMPLATE_STARTSYMBOLVALUE="start_symbol_value";
    public static final String NONREC_PARSER_GRAMMARGENERATOR_PRODUCTION_RULE="PRODUCTION_RULE";
    public static final String NONREC_PARSER_GRAMMARGENERATOR_EMPTY_SYMBOL="EMPTY_SYMBOL";
    public static final String NONREC_PARSER_GRAMMARGENERATOR_NONTERMINAL="NONTERMINAL";
    public static final String NONREC_PARSER_GRAMMARGENERATOR_INIT_INPUT_LEX="# ignorables\r\n" + //
                "%\" \", \"\\t\", \"\\n" + //
                "\", \"\\s\", \"\\r\"\r\n" + //
                "# definitions (top-down priority)\r\n" + //
                "# > ASSIGNMENTS\r\n" + //
                "# VOCABULARY_ASSIGNMENT := \"V :=\"\r\n" + //
                "# ALPHABET_ASSIGNMENT := \"A :=\"\r\n" + //
                "# STARTSYMBOL_ASSIGNMENT := \"S :=\"\r\n" + //
                "# Sets have a special syntax. For example: {'S', 'a'} is defined as %'S', 'a' (like the ignorables set)\r\n" + //
                "# SET := \"%( *\\\"([a-zA-Z0-9_]*)\\\" *,)*( *\\\"([a-zA-Z0-9_]*)\\\" *)\"\r\n" + //
                "# > RULES\r\n" + //
                "NONTERMINAL := \"[a-zA-Z0-9_]+\"\r\n" + //
                "# PRODUCTION_SYMBOL := \"\\->\"\r\n" + //
                "# PRODUCTION_OR := \"\\|\"\r\n" + //
                "PRODUCTION_RULE := \"((\\-> *)|(\\| *))([\\$a-zA-Z0-9\\(\\)\\{\\}\\[\\]\\^\\?\\:\\\\\\/\\!\\.;\\-_<>\\\"\\'\\`\\~=&\\*#@\\+ ]+)\"\r\n" + //
                "EMPTY_SYMBOL := \"$\"";


    public static final String BOTTOM_UP_PARSER_ERROR_NO_SLR1_GRAMMAR="Grammar is no SLR1";
            
    public static final String GRAMMAR_ERROR_INVALID_SYMBOL="String is not a symbol";

    public static final String TEMPLATE = "{{package_name}};\r\n" + //
                "\r\n" + //
                "import java.util.ArrayList;\r\n" + //
                "import java.util.List;\r\n" + //
                "import java.util.regex.Matcher;\r\n" + //
                "import java.util.regex.Pattern;\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "/**\r\n" + //
                " * A non-recursive predictive parser.\r\n" + //
                " */\r\n" + //
                "@SuppressWarnings(\"unchecked\")\r\n" + //
                "public final class {{parser_name}} {\r\n" + //
                "    private static final String EMPTY_SYMBOL = \"$\";\r\n" + //
                "    private static final Token EMPTY_TOKEN = new Token(new TokenType(\"EMPTY_SYMBOL\", \"\\\\$\", 0, false), EMPTY_SYMBOL);\r\n" + //
                "\r\n" + //
                "    private final List<Production>[][] parseTable = new ArrayList[{{parsetable_y_size}}][{{parsetable_x_size}}];\r\n" + //
                "    private final Stack<Node> stack = new Stack<>();\r\n" + //
                "    private final List<TokenType> tokentypes = new ArrayList<>();\r\n" + //
                "    private final List<Token> alphabet = new ArrayList<>();\r\n" + //
                "    private final List<Token> vocabulary = new ArrayList<>();\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Creates a new parser.\r\n" + //
                "     */\r\n" + //
                "    public {{parser_name}}() {\r\n" + //
                "        initParsetable();\r\n" + //
                "        initTokentypes();\r\n" + //
                "        initAlphabet();\r\n" + //
                "        initVocabulary();\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Parses an input word and returns the root node.\r\n" + //
                "     * @param w Input word.\r\n" + //
                "     * @return The root node of the generated parsetree.\r\n" + //
                "     * @throws ParseException If there is any error with parsing the given input word.\r\n" + //
                "     */\r\n" + //
                "    public Node parse(final String w) throws ParseException {\r\n" + //
                "        final String w$ = w + EMPTY_SYMBOL;\r\n" + //
                "        final Node root = new Node(new Token(new TokenType(\"{{start_symbol_name}}\", \"{{start_symbol_regex}}\", 0, false), \"{{start_symbol_value}}\"));\r\n" + //
                "        final Node empty = new Node(EMPTY_TOKEN);\r\n" + //
                "        \r\n" + //
                "        stack.clean();\r\n" + //
                "        stack.push(empty);\r\n" + //
                "        stack.push(root);\r\n" + //
                "\r\n" + //
                "        computeSystemAnalysis(w$, root);\r\n" + //
                "\r\n" + //
                "        return root;\r\n" + //
                "    }   \r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Compute the system analysis: Checks if a word is valid according to the grammar and in the language of the grammar.\r\n" + //
                "     * It directly changes the given root.\r\n" + //
                "     * @param w$ Modified input word.\r\n" + //
                "     * @param root The root node of the parsetree.\r\n" + //
                "     * @throws ParseException If there is any error with parsing the given input word.\r\n" + //
                "     */\r\n" + //
                "    private void computeSystemAnalysis(final String w$, final Node root) throws ParseException {\r\n" + //
                "        root.cleanChildren();\r\n" + //
                "\r\n" + //
                "        int ip = 0; // first symbol of w$\r\n" + //
                "        Node X = stack.peek(); // top of stack\r\n" + //
                "        Node a = new Node(getNextToken(ip, w$)); // symbol of w$\r\n" + //
                "        Node cursor = root;\r\n" + //
                "\r\n" + //
                "        do {\r\n" + //
                "            a = new Node(getNextToken(ip, w$));\r\n" + //
                "            cursor = X;\r\n" + //
                "            \r\n" + //
                "            if(a.getToken() == null) throw new ParseException(\"Invalid Token found at position: \" + ip + \"\\n" + //
                "\\n" + //
                "\" + w$);\r\n" + //
                "\r\n" + //
                "            if(a.getToken().tokenType().ignore() && ip < w$.length() - 1) { // ignore token (e.g. whitespace, empty symbol except the last one ...) \r\n" + //
                "                ip += a.getToken().symbol().length();\r\n" + //
                "                continue;\r\n" + //
                "            }\r\n" + //
                "\r\n" + //
                "            if(isSymbolTerminal(X.getToken().symbol()) || X.getToken().symbol().equals(EMPTY_SYMBOL)) {\r\n" + //
                "                if(X.equals(a)) {\r\n" + //
                "                    stack.pop();\r\n" + //
                "\r\n" + //
                "                    updateTree(cursor.getParent(), a);\r\n" + //
                "\r\n" + //
                "                    ip += a.getToken().symbol().length();\r\n" + //
                "                } else throw new ParseException(stack.toString());\r\n" + //
                "            } else { \r\n" + //
                "                try {\r\n" + //
                "                    final Production production = parseTable[vocabulary.indexOf(X.getToken())][alphabet.indexOf(a.getToken())].get(0);\r\n" + //
                "\r\n" + //
                "                    if(production == null) throw new ParseException(\"Parsetable error\");\r\n" + //
                "\r\n" + //
                "                    stack.pop();\r\n" + //
                "                    \r\n" + //
                "                    final Token[] rhs = production.getRhs().get(0); \r\n" + //
                "                    for(int i = rhs.length - 1; i >= 0; i--) {\r\n" + //
                "                        final Token tempToken = rhs[i];\r\n" + //
                "                        final Node tempNode = new Node(tempToken);\r\n" + //
                "\r\n" + //
                "                        updateTree(cursor, tempNode);\r\n" + //
                "\r\n" + //
                "                        stack.push(tempNode); \r\n" + //
                "                    }\r\n" + //
                "                } catch (IndexOutOfBoundsException e) {\r\n" + //
                "                    throw new ParseException(\"Parsetable error\");\r\n" + //
                "                }\r\n" + //
                "            }\r\n" + //
                "\r\n" + //
                "            while(stack.peek().getToken().equals(EMPTY_TOKEN) && stack.size() > 1) updateTree(stack.peek().getParent(), stack.pop());\r\n" + //
                "        } while(!((X = stack.peek()).getToken().equals(EMPTY_TOKEN) && a.getToken().equals(EMPTY_TOKEN))); // stack and input buffer is empty\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Eats the next token from the input buffer.\r\n" + //
                "     * @return The next token from the input buffer or null.\r\n" + //
                "     */\r\n" + //
                "    private Token getNextToken(final int ip, final String w$) {\r\n" + //
                "        final String tempW$ = w$.substring(ip);\r\n" + //
                "\r\n" + //
                "        for(final TokenType tokenType : tokentypes) { \r\n" + //
                "            if(tokenType.name().equals(\"NONTERMINAL\")) continue; // TODO: Add more nonterminal labels\r\n" + //
                "            final Pattern pattern = Pattern.compile(\"^\" + \"(\" +  tokenType.regex() + \")\");\r\n" + //
                "            final Matcher matcher = pattern.matcher(tempW$);\r\n" + //
                "\r\n" + //
                "            if(matcher.find()) {\r\n" + //
                "                final String value = matcher.group();\r\n" + //
                "                return new Token(tokenType, value);\r\n" + //
                "            }\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        return null;\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Checks if the given symbol is a terminal.\r\n" + //
                "     * @param symbol The symbol which is checked.\r\n" + //
                "     * @return True if the symbol is a terminal, false otherwise.\r\n" + //
                "     */\r\n" + //
                "    private boolean isSymbolTerminal(final String symbol) {\r\n" + //
                "        return alphabet.stream()\r\n" + //
                "                        .map(e -> e.symbol())\r\n" + //
                "                        .anyMatch(e -> e.equals(symbol))\r\n" + //
                "            || alphabet.stream()\r\n" + //
                "                        .map(e -> e.tokenType().regex())\r\n" + //
                "                        .anyMatch(e -> symbol.matches(\"^\" + e + \"$\"));\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Update the tree.\r\n" + //
                "     * @param root The root of the partition.\r\n" + //
                "     * @param node The token to add.\r\n" + //
                "     */\r\n" + //
                "    private void updateTree(final Node root, final Node node) {\r\n" + //
                "        root.addChild(node);\r\n" + //
                "        node.setParent(root);\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * ================================\r\n" + //
                "     * INITS\r\n" + //
                "     * ================================\r\n" + //
                "     */\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Inits the {@link {{parser_name}}#parseTable}.\r\n" + //
                "     */\r\n" + //
                "    private void initParsetable() {\r\n" + //
                "        {{init_parsetable}}\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Inits the {@link {{parser_name}}#tokentypes}.\r\n" + //
                "     */\r\n" + //
                "    private void initTokentypes() {\r\n" + //
                "        {{init_tokentypes}}\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Inits the {@link {{parser_name}}#alphabet}.\r\n" + //
                "     */\r\n" + //
                "    private void initAlphabet() {\r\n" + //
                "        {{init_alphabet}}\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Inits the {@link {{parser_name}}#vocabulary}.\r\n" + //
                "     */\r\n" + //
                "    private void initVocabulary() {\r\n" + //
                "        {{init_vocabulary}}\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * ================================\r\n" + //
                "     * UTILS\r\n" + //
                "     * ================================\r\n" + //
                "     */\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Defines a token for the lexical analysis.\r\n" + //
                "     */\r\n" + //
                "    private final record Token(TokenType tokenType, String symbol) {\r\n" + //
                "        @Override public boolean equals(Object obj) {\r\n" + //
                "            if (obj instanceof Token) {\r\n" + //
                "                Token other = (Token)obj;\r\n" + //
                "                return this.tokenType.equals(other.tokenType) && this.symbol.equals(other.symbol);\r\n" + //
                "            }\r\n" + //
                "\r\n" + //
                "            return false;\r\n" + //
                "        }\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Defines a tokentype for the lexical analysis.\r\n" + //
                "     */\r\n" + //
                "    private final record TokenType(String name, String regex, int priority, boolean ignore) {\r\n" + //
                "        @Override public boolean equals(Object obj) {\r\n" + //
                "            if (obj instanceof TokenType) {\r\n" + //
                "                TokenType other = (TokenType)obj;\r\n" + //
                "                return this.name.equals(other.name);\r\n" + //
                "            }\r\n" + //
                "\r\n" + //
                "            return false;\r\n" + //
                "        }\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Defines a new node for the parsetree.\r\n" + //
                "     */\r\n" + //
                "    private final class Node {\r\n" + //
                "        private final Token token;\r\n" + //
                "        private final List<Node> children;\r\n" + //
                "\r\n" + //
                "        private Node parent;\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Creates a new node.\r\n" + //
                "        * @param token Token of the node.\r\n" + //
                "        * @param parent Parent of the node.\r\n" + //
                "        * @param children Children of the node.\r\n" + //
                "        */\r\n" + //
                "        public Node(final Token token, final Node parent, final List<Node> children) {\r\n" + //
                "            this.token = token;\r\n" + //
                "            this.parent = parent;\r\n" + //
                "            this.children = children;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Creates a new root node.\r\n" + //
                "        * @param token Token of the node.\r\n" + //
                "        */\r\n" + //
                "        public Node(final Token token) {\r\n" + //
                "            this(token, null, new ArrayList<>());\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        public Token getToken() {\r\n" + //
                "            return token;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        public Node getParent() {\r\n" + //
                "            return parent;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        public List<Node> getChildren() {\r\n" + //
                "            return children;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        public void setParent(final Node parent) {\r\n" + //
                "            this.parent = parent;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        \r\n" + //
                "        /**\r\n" + //
                "        * Clean the children of the node.\r\n" + //
                "        */\r\n" + //
                "        public void cleanChildren() {\r\n" + //
                "            children.clear();\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Add a child to the node if its not already a child.\r\n" + //
                "        * @param child Child to add.\r\n" + //
                "        */\r\n" + //
                "        public void addChild(final Node child) {\r\n" + //
                "            if(!children.contains(child)) children.add(child);\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Remove a child from the node.\r\n" + //
                "        * @param child Child to remove.\r\n" + //
                "        */\r\n" + //
                "        public void removeChild(final Node child) {\r\n" + //
                "            children.remove(child);\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Check if the node is a leaf.\r\n" + //
                "        * @return True if the node is a leaf, false otherwise.\r\n" + //
                "        */\r\n" + //
                "        public boolean isLeaf() {\r\n" + //
                "            return children.isEmpty();\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Accept a visitor.\r\n" + //
                "        * @param vis The visitor to accept.\r\n" + //
                "        */\r\n" + //
                "        public void accept(final Visitor vis) {\r\n" + //
                "            vis.visit(this);\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        @Override public boolean equals(final Object obj) {\r\n" + //
                "            if(obj instanceof Node) {\r\n" + //
                "                final Node other = (Node) obj;\r\n" + //
                "                return this.token.equals(other.token);\r\n" + //
                "            }\r\n" + //
                "\r\n" + //
                "            return false;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        @Override public String toString() {\r\n" + //
                "            return token.toString();\r\n" + //
                "        }\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Defines the production structure.\r\n" + //
                "     */\r\n" + //
                "    private final class Production {\r\n" + //
                "        private final Token lhs;\r\n" + //
                "        private final List<Token[]> rhs;\r\n" + //
                "\r\n" + //
                "    \r\n" + //
                "        /**\r\n" + //
                "         * Creates a new production.\r\n" + //
                "         */\r\n" + //
                "        public Production(final Token lhs, final List<Token[]> rhs) {\r\n" + //
                "            this.lhs = lhs;\r\n" + //
                "            this.rhs = rhs;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        @Override public String toString() {\r\n" + //
                "            final StringBuilder sb = new StringBuilder();\r\n" + //
                "\r\n" + //
                "            sb.append(lhs.symbol()).append(\" -> \");\r\n" + //
                "\r\n" + //
                "            for(int i = 0; i < rhs.size(); i++) {\r\n" + //
                "                for(int j = 0; j < rhs.get(i).length; j++) {\r\n" + //
                "                    sb.append(rhs.get(i)[j].symbol());\r\n" + //
                "                }\r\n" + //
                "                sb.append(\" \");\r\n" + //
                "                if(i < rhs.size() - 1) sb.append(\"| \");\r\n" + //
                "            }\r\n" + //
                "\r\n" + //
                "            return sb.toString();\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        public Token getLhs() {\r\n" + //
                "            return lhs;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        public List<Token[]> getRhs() {\r\n" + //
                "            return rhs;\r\n" + //
                "        }\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Defines a stack.\r\n" + //
                "     * @param <T> Context (contenttype) of the stack.\r\n" + //
                "     */\r\n" + //
                "    public class Stack<T> {\r\n" + //
                "        private List<T> stack;\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Creates a new stack.\r\n" + //
                "        */\r\n" + //
                "        public Stack() {\r\n" + //
                "            clean();\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Push a token to the stack.\r\n" + //
                "        * @param token The token to push.\r\n" + //
                "        */\r\n" + //
                "        public void push(final T token) {\r\n" + //
                "            stack.add(token);\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Pop a token from the stack.\r\n" + //
                "        * @return The popped token.\r\n" + //
                "        */\r\n" + //
                "        public T pop() {\r\n" + //
                "            return stack.remove(stack.size() - 1);\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Peek the top token from the stack.\r\n" + //
                "        * @return The peeked token.\r\n" + //
                "        */\r\n" + //
                "        public T peek() {\r\n" + //
                "            return stack.get(stack.size() - 1);\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Check if the stack is empty.\r\n" + //
                "        * @return True if the stack is empty, false otherwise.\r\n" + //
                "        */\r\n" + //
                "        public boolean isEmpty() {\r\n" + //
                "            return stack.isEmpty();\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Get the size of the stack.\r\n" + //
                "        * @return The size of the stack.\r\n" + //
                "        */\r\n" + //
                "        public int size() {\r\n" + //
                "            return stack.size();\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "        /**\r\n" + //
                "        * Cleans the stack.\r\n" + //
                "        */\r\n" + //
                "        public void clean() {\r\n" + //
                "            stack = new ArrayList<>();\r\n" + //
                "        } \r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        @Override public String toString() {\r\n" + //
                "            return stack.stream()\r\n" + //
                "                        .map(Object::toString)\r\n" + //
                "                        .reduce((a, b) -> a + \"\\n" + //
                "+ \" + b)\r\n" + //
                "                        .orElse(\"\");\r\n" + //
                "        }\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Exception for parsing errors.\r\n" + //
                "     */\r\n" + //
                "    public class ParseException extends Exception {\r\n" + //
                "        public ParseException(final String message) {\r\n" + //
                "            super(message);\r\n" + //
                "        }\r\n" + //
                "        public ParseException() {\r\n" + //
                "            super();\r\n" + //
                "        }\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Visitor interface.\r\n" + //
                "     */\r\n" + //
                "    public interface Visitor {\r\n" + //
                "        /**\r\n" + //
                "        * Visit the object.\r\n" + //
                "        * @param obj Object to visit.\r\n" + //
                "        */\r\n" + //
                "        public void visit(final Node obj);\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * ================================\r\n" + //
                "     * CUSTOMIZABLE\r\n" + //
                "     * ================================\r\n" + //
                "     */\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "    /**\r\n" + //
                "     * Example visitor for {@link Node}.\r\n" + //
                "     * It formats the parsetree to a json.\r\n" + //
                "     */\r\n" + //
                "    /*\r\n" + //
                "    private final class JsonTreeVisitor implements Visitor {\r\n" + //
                "        private final JSONObject json;\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        public JsonTreeVisitor() {\r\n" + //
                "            this.json = new JSONObject();\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        public JSONObject getJson() {\r\n" + //
                "            return json;\r\n" + //
                "        }\r\n" + //
                "\r\n" + //
                "\r\n" + //
                "        @Override public void visit(final Node node) {\r\n" + //
                "            final JSONArray kids = new JSONArray();\r\n" + //
                "\r\n" + //
                "            if (Node.getChildren().size() > 0) {\r\n" + //
                "                for (Node child : node.getChildren()) {\r\n" + //
                "                    final JsonTreeVisitor visitor = new JsonTreeVisitor();\r\n" + //
                "                    child.accept(visitor);\r\n" + //
                "                    kids.put(visitor.getJson());\r\n" + //
                "                }\r\n" + //
                "            } \r\n" + //
                "\r\n" + //
                "            this.json.put(node.getToken().symbol(), kids);\r\n" + //
                "        }\r\n" + //
                "    }\r\n" + //
                "    */\r\n" + //
                "}";
}
